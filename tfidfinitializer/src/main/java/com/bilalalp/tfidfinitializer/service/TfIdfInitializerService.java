package com.bilalalp.tfidfinitializer.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.*;
import com.bilalalp.common.service.*;
import com.bilalalp.tfidfinitializer.amqp.MessageSender;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TfIdfInitializerService implements MessageListener {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private TfIdfProcessInfoService tfIdfProcessInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AnalyzableWordInfoService analyzableWordInfoService;

    @Autowired
    private WordEliminationService wordEliminationService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MessageSender messageSender;

    @Qualifier("tfIdfProcessQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private TvProcessInfoService tvProcessInfoService;

    @Transactional
    public void process(final Long id) {

        try {
            final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(id);
            final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfRequestInfo.getLinkSearchRequestInfo();

//        saveWords(tfIdfRequestInfo, linkSearchRequestInfo);
            saveWordsForRange(tfIdfRequestInfo);

            arrangePatents(tfIdfRequestInfo, linkSearchRequestInfo);

        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void saveWordsForRange(final TfIdfRequestInfo tfIdfRequestInfo) {

//        final List<Long> patentIds = getPatentIds();

        final List<TvProcessInfo> tvProcessInfoList = tvProcessInfoService.findByLimit(tfIdfRequestInfo.getThresholdValue().intValue());

//        List<TvProcessInfo> byLimit = tvProcessInfoService.findByLimit(tfIdfRequestInfo.getThresholdValue().intValue(), SplitWordType.TWO, patentIds);
//        List<TvProcessInfo> byLimit1 = tvProcessInfoService.findByLimit(tfIdfRequestInfo.getThresholdValue().intValue(), SplitWordType.ONE, patentIds);
//        final List<TvProcessInfo> tvProcessInfoList = new ArrayList<>();
//        tvProcessInfoList.addAll(tvProcessInfoList);
//        tvProcessInfoList.addAll(tvProcessInfoList);

//        final List<BigInteger> words = splitWordInfoService.getWords(tfIdfRequestInfo.getLinkSearchRequestInfo().getId());

        for (final TvProcessInfo tvProcessInfo : tvProcessInfoList) {
            final AnalyzableWordInfo analyzableWordInfo = new AnalyzableWordInfo();
            analyzableWordInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
            analyzableWordInfo.setWordId(tvProcessInfo.getWordId());
            applicationContext.getBean(TfIdfInitializerService.class).save(analyzableWordInfo);
        }
    }

    private List<Long> arrangePatents(final TfIdfRequestInfo tfIdfRequestInfo, final LinkSearchRequestInfo linkSearchRequestInfo) {

//        final List<Long> patentIds = patentInfoService.getPatentIds(linkSearchRequestInfo.getId());

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2015);

        final List<Long> patentIds = patentInfoService.getPatentIdsByDate(calendar.getTime());

        final List<Long> germanPatentIds = getGermanPatentIds();

//        final List<Long> patentIds = getPatentIds();

        patentIds.stream().filter(id -> !germanPatentIds.contains(id)).forEach(id -> {
            final TfIdfProcessInfo tfIdfProcessInfo = new TfIdfProcessInfo();
            tfIdfProcessInfo.setLinkSearchRequestInfo(linkSearchRequestInfo);
            tfIdfProcessInfo.setPatentInfoId(id);
            tfIdfProcessInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
            tfIdfProcessInfo.setThresholdValue(tfIdfRequestInfo.getThresholdValue());
            applicationContext.getBean(TfIdfInitializerService.class).saveAndSendToQueue(tfIdfProcessInfo);
        });

        return patentIds;
    }

    private List<Long> getPatentIds() {
        final List<Long> patentIds = new ArrayList<>();
        try {
            final List<String> collect = Files.lines(Paths.get("C:\\patentdoc\\random-patents.txt")).collect(Collectors.toList());
            patentIds.addAll(collect.stream().map(Long::valueOf).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patentIds;
    }

    private List<Long> getGermanPatentIds() {
        final List<Long> patentIds = new ArrayList<>();
        try {
            final List<String> collect = Files.lines(Paths.get("C:\\patentdoc\\german-patents.txt")).collect(Collectors.toList());
            patentIds.addAll(collect.stream().map(Long::valueOf).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patentIds;
    }

    private void saveWords(TfIdfRequestInfo tfIdfRequestInfo, LinkSearchRequestInfo linkSearchRequestInfo) {
        Long start = 0L;
        Long pageSize = 10000L;

        Long thValue = 0L;

        boolean result = true;

        while (result) {

            final Pageable pageable = new PageRequest(start.intValue(), pageSize.intValue());
            final List<WordElimination> wordEliminationList = wordEliminationService.getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(linkSearchRequestInfo, pageable);

            start += 1L;

            if (CollectionUtils.isEmpty(wordEliminationList)) {
                break;
            }

            for (final WordElimination wordElimination : wordEliminationList) {

                final AnalyzableWordInfo analyzableWordInfo = new AnalyzableWordInfo();
                analyzableWordInfo.setTfIdfRequestInfo(tfIdfRequestInfo);
                analyzableWordInfo.setWordId(wordElimination.getWordInfoId());
                applicationContext.getBean(TfIdfInitializerService.class).save(analyzableWordInfo);

                thValue++;

                if (thValue.compareTo(tfIdfRequestInfo.getThresholdValue()) == 0) {
                    result = false;
                    break;
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(final AnalyzableWordInfo analyzableWordInfo) {
        analyzableWordInfoService.save(analyzableWordInfo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAndSendToQueue(final TfIdfProcessInfo tfIdfProcessInfo) {
        tfIdfProcessInfoService.save(tfIdfProcessInfo);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(tfIdfProcessInfo.getId()));
    }

    @Transactional
    @Override
    public void onMessage(final Message message) {
        try {
            final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
            process(queueMessageDto.getId());
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}