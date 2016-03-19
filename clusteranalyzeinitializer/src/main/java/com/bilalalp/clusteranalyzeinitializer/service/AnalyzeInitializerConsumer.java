package com.bilalalp.clusteranalyzeinitializer.service;

import com.bilalalp.clusteranalyzeinitializer.amqp.MessageSender;
import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.cluster.ClusterAnalyzingProcessInfo;
import com.bilalalp.common.entity.cluster.ClusterAnalyzingRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AnalyzeInitializerConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ClusterAnalyzingRequestInfoService clusterAnalyzingRequestInfoService;

    @Autowired
    private ClusteringRequestInfoService clusteringRequestInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private ClusterAnalyzingProcessInfoService clusterAnalyzingProcessInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier("cprQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Autowired
    private ApplicationContext applicationContext;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        try {
            analyze(((QueueMessageDto) messageConverter.fromMessage(message)).getId());
        } catch (final Exception ex) {
            System.out.println(ex);
            log.error(ex.getMessage(), ex);
        }
    }

    private void analyze(final Long clusteringRequestInfoId) {
        final ClusterAnalyzingRequestInfo clusterAnalyzingRequestInfo = clusterAnalyzingRequestInfoService.find(clusteringRequestInfoId);
        final ClusteringRequestInfo clusteringRequestInfo = clusteringRequestInfoService.find(clusterAnalyzingRequestInfo.getClusteringRequestInfoId());
        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());
        final Long lsrId = tfIdfRequestInfo.getLinkSearchRequestInfo().getId();

        final int clusterNumber = clusteringRequestInfo.getClusterNumber().intValue();
        final Long wordLimit = clusterAnalyzingRequestInfo.getWordLimit();

        for (long i = 1L; i <= clusterNumber; i++) {
            final long finalI = i;

            final List<String> words = splitWordInfoService.getWordsByClusterIdAndLimit(clusteringRequestInfo.getId(), i, wordLimit);
            final List<Long> wordIds = wordSummaryInfoService.getWordIds(lsrId, words);

            wordIds.stream().forEach(k -> applicationContext.getBean(AnalyzeInitializerConsumer.class).createProcessInfo(clusteringRequestInfoId, finalI, k));
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProcessInfo(final Long clusteringRequestInfoId, final long finalI, Long k) {
        final ClusterAnalyzingProcessInfo clusterAnalyzingProcessInfo = new ClusterAnalyzingProcessInfo();
        clusterAnalyzingProcessInfo.setClusterAnalyzingRequestInfoId(clusteringRequestInfoId);
        clusterAnalyzingProcessInfo.setClusterNumber(finalI);
        clusterAnalyzingProcessInfo.setWordId(k);
        clusterAnalyzingProcessInfoService.saveInNewTransaction(clusterAnalyzingProcessInfo);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(clusterAnalyzingProcessInfo.getId()));
    }
}