package com.bilalalp.clusteranalyzerprocessor.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.cluster.ClusterAnalyzingProcessInfo;
import com.bilalalp.common.entity.cluster.ClusterAnalyzingRequestInfo;
import com.bilalalp.common.entity.cluster.ClusterAnalyzingResultInfo;
import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AnalyzeProcessorConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ClusterAnalyzingProcessInfoService clusterAnalyzingProcessInfoService;

    @Autowired
    private ClusterAnalyzingRequestInfoService clusterAnalyzingRequestInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private ClusterAnalyzingResultInfoService clusterAnalyzingResultInfoService;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        try {
            process(((QueueMessageDto) messageConverter.fromMessage(message)).getId());
        } catch (final Exception ex) {
            System.out.println(ex);
            log.error(ex.getMessage(), ex);
        }
    }

    private void process(final Long clusterAnalyzingProcessInfoId) {

        final ClusterAnalyzingProcessInfo clusterAnalyzingProcessInfo = clusterAnalyzingProcessInfoService.find(clusterAnalyzingProcessInfoId);
        final Long clusterAnalyzingRequestInfoId = clusterAnalyzingProcessInfo.getClusterAnalyzingRequestInfoId();
        final ClusterAnalyzingRequestInfo clusterAnalyzingRequestInfo = clusterAnalyzingRequestInfoService.find(clusterAnalyzingRequestInfoId);
        final Long clusteringRequestInfoId = clusterAnalyzingRequestInfo.getClusteringRequestInfoId();
        final Long clusterNumber = clusterAnalyzingProcessInfo.getClusterNumber();
        final Long wordId = clusterAnalyzingProcessInfo.getWordId();

        final Long wordCountInACluster = splitWordInfoService.getWordCountInACluster(clusterNumber, clusteringRequestInfoId, wordId);
        final Long patentCountWithOutClusterNumber = clusterResultInfoService.getPatentCountWithOutClusterNumber(clusterNumber);
        final Long totalPatentCountInOtherClusters = splitWordInfoService.getTotalPatentCountInOtherClusters(clusterNumber, clusteringRequestInfoId, wordId);

        final double result = getResult(wordCountInACluster, patentCountWithOutClusterNumber, totalPatentCountInOtherClusters);

        final ClusterAnalyzingResultInfo clusterAnalyzingResultInfo = new ClusterAnalyzingResultInfo();
        clusterAnalyzingResultInfo.setClusterAnalyzingProcessInfoId(clusterAnalyzingProcessInfoId);
        clusterAnalyzingResultInfo.setClusteringRequestId(clusteringRequestInfoId);
        clusterAnalyzingResultInfo.setClusterNumber(clusterNumber);
        clusterAnalyzingResultInfo.setResult(result);
        clusterAnalyzingResultInfo.setPatentCountWithOutCluster(patentCountWithOutClusterNumber);
        clusterAnalyzingResultInfo.setWordId(wordId);
        clusterAnalyzingResultInfo.setWordCountInCluster(wordCountInACluster);
        clusterAnalyzingResultInfo.setTotalPatentCountInOtherCluster(totalPatentCountInOtherClusters);
        clusterAnalyzingResultInfoService.save(clusterAnalyzingResultInfo);
    }

    private double getResult(Long wordCountInACluster, Long patentCountWithOutClusterNumber, Long totalPatentCountInOtherClusters) {
        if(totalPatentCountInOtherClusters == 0L){
            return 0L;
        }
        return wordCountInACluster * Math.log(patentCountWithOutClusterNumber / totalPatentCountInOtherClusters);
    }
}