package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.tfidf.TfIdfInfo;
import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.repository.TfIdfInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
public class TfIdfInfoServiceImpl extends AbstractService<TfIdfInfo> implements TfIdfInfoService {

    @Autowired
    private TfIdfInfoRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private WordEliminationService wordEliminationService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private AnalyzableWordInfoService analyzableWordInfoService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final WordElimination wordElimination, final LinkSearchRequestInfo linkSearchRequestInfo, final PatentWordCountDto patentWordCountDto, final Long thresholdValue) {

        final PatentInfo patentInfo = new PatentInfo();
        patentInfo.setId(patentWordCountDto.getPatentId());
        patentInfo.setVersion(patentWordCountDto.getPatentVersion());

        final Double tfIdfValue = patentWordCountDto.getWordCount() * wordElimination.getLogValue();

        final TfIdfInfo tfIdfInfo = new TfIdfInfo();
        tfIdfInfo.setPatentInfoId(patentInfo.getId());
        tfIdfInfo.setCount(patentWordCountDto.getWordCount());
        tfIdfInfo.setDfValue(wordElimination.getDfValue());
        tfIdfInfo.setLinkSearchRequestInfoId(linkSearchRequestInfo.getId());
        tfIdfInfo.setLogValue(wordElimination.getLogValue());
        tfIdfInfo.setPatentCount(wordElimination.getPatentCount());
        tfIdfInfo.setScore(tfIdfValue);
        tfIdfInfo.setTfValue(patentWordCountDto.getWordCount());
        tfIdfInfo.setThresholdValue(thresholdValue);
        tfIdfInfo.setWordInfoId(wordElimination.getWordInfoId());
        save(tfIdfInfo);
    }

    @Override
    public void processEliminatedWord(final TfIdfProcessInfo tfIdfProcessInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfProcessInfo.getTfIdfRequestInfo();
        final Long patentInfoId = tfIdfProcessInfo.getPatentInfoId();
        final List<PatentWordCountDto> wordCount = splitWordInfoService.getWordCount(patentInfoId);

        final List<Long> wordIdList = wordCount.stream().map(PatentWordCountDto::getPatentId).collect(Collectors.toList());

        final List<Long> wordIds = getWordIds(tfIdfProcessInfo, wordIdList);

        final List<PatentWordCountDto> totalPatentWordCountDtoList = createEmptyList(wordIds);
        totalPatentWordCountDtoList.addAll(wordCount);

        Collections.sort(totalPatentWordCountDtoList, (o1, o2) -> o1.getPatentId().compareTo(o2.getPatentId()));

        writeToFile(patentInfoId, totalPatentWordCountDtoList, tfIdfRequestInfo);
    }

    private void writeToFile(final long patentInfoId, final List<PatentWordCountDto> patentWordCountDtoList, final TfIdfRequestInfo tfIdfRequestInfo) {

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tfIdfRequestInfo.getFileName(), true)))) {
            out.println(getFormattedLine(patentInfoId, patentWordCountDtoList));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validate(String s) {
        try {
            final String[] split = s.split("\\$");
            double[] values = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                values[i] = Double.parseDouble(split[i].split(":")[1]);
            }
            return true;
        } catch (final Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    private String getFormattedLine(long patentInfoId, List<PatentWordCountDto> patentWordCountDtoList) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(patentInfoId).append("::");
        int k = 0;
        for (int i = 0; i < patentWordCountDtoList.size(); i++) {
            final PatentWordCountDto patentWordCountDto = patentWordCountDtoList.get(i);
            stringBuilder.append(patentWordCountDto.getPatentId()).append(":").append(patentWordCountDto.getWordCount());

            if (k != patentWordCountDtoList.size() - 2) {
                stringBuilder.append("$");
            }
        }

        return stringBuilder.toString();
    }

    private List<Long> getWordIds(TfIdfProcessInfo tfIdfProcessInfo, List<Long> wordIdList) {
        if (CollectionUtils.isNotEmpty(wordIdList)) {
            return analyzableWordInfoService.getWordIds(tfIdfProcessInfo.getTfIdfRequestInfo(), wordIdList);
        } else {
            return analyzableWordInfoService.getWordIds(tfIdfProcessInfo.getTfIdfRequestInfo());
        }
    }

    private List<PatentWordCountDto> createEmptyList(final List<Long> wordIds) {

        final List<PatentWordCountDto> patentWordCountDtoList = new ArrayList<>();

        for (final Long id : wordIds) {
            final PatentWordCountDto patentWordCountDto = new PatentWordCountDto();
            patentWordCountDto.setPatentId(id);
            patentWordCountDto.setWordCount(0L);
            patentWordCountDtoList.add(patentWordCountDto);
        }

        return patentWordCountDtoList;
    }

    @Transactional
    @Override
    public void exportToFile(TfIdfProcessInfo tfIdfProcessInfo) {
//        final LinkSearchRequestInfo linkSearchRequestInfo = tfIdfProcessInfo.getLinkSearchRequestInfo();
//        final WordElimination wordElimination = tfIdfProcessInfo.getWordElimination();
//        final List<PatentWordCountDto> patentWordCountDtoList = splitWordInfoService.getPatentWordCount(linkSearchRequestInfo, wordElimination.getWordInfoId());
//        final List<EntityDto> patentInfoIds = patentInfoService.getPatentInfos(linkSearchRequestInfo.getId(), wordElimination.getWordInfoId());

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveWithNewTransaction(final TfIdfProcessInfo tfIdfProcessInfo, final List<EntityDto> patentInfoIds) {

    }
}