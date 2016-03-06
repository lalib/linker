package com.bilalalp.parser.service;

import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.entity.patent.SplitWordType;
import com.bilalalp.common.service.SplitWordInfoService;
import com.bilalalp.common.service.WordInfoService;
import com.bilalalp.parser.config.ParserInitialRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private ParserInitialRepository parserInitialRepository;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private LemmatizerService lemmatizerService;

    @Autowired
    private WordInfoService wordInfoService;

    @Override
    public void parse(final PatentInfo patentInfo) {

        final String abstractContent = patentInfo.getAbstractContent();
        if (StringUtils.isNotEmpty(abstractContent)) {
            final String removedPunctuations = removePunctuations(abstractContent);
            final String lemmatizedWords = lemmatizerService.lemmatize(removedPunctuations);
            final String removedStopWords = removeStopWords(lemmatizedWords);
            final String removedMultipleSpaces = removeMultipleSpaces(removedStopWords);
            if (StringUtils.isEmpty(removedMultipleSpaces.trim())) {
                return;
            }
            final List<String> splitBySpaceList = createNGram(1, removedMultipleSpaces);
            final List<String> splitByTwoWords = createNGram(2, removedMultipleSpaces);

            final List<SplitWordInfo> oneGramSplitWordInfos = createNGramSplitWords(splitBySpaceList, patentInfo, SplitWordType.ONE);
            final List<SplitWordInfo> twoGramSplitWordInfos = createNGramSplitWords(splitByTwoWords, patentInfo, SplitWordType.TWO);

            final List<SplitWordInfo> splitWordInfoList = new ArrayList<>();
            splitWordInfoList.addAll(oneGramSplitWordInfos);
            splitWordInfoList.addAll(twoGramSplitWordInfos);
            splitWordInfoService.save(splitWordInfoList);
        }
    }

    private List<SplitWordInfo> createNGramSplitWords(final List<String> splitBySpaceList, final PatentInfo patentInfo, final SplitWordType splitWordType) {
        return splitBySpaceList.stream().map(k -> createSplitWordInfo(patentInfo, splitWordType, k)).collect(Collectors.toList());
    }

    private SplitWordInfo createSplitWordInfo(final PatentInfo patentInfo, final SplitWordType splitWordType, final String k) {
        final Long wordId = wordInfoService.insertInfoExists(k);
        final SplitWordInfo splitWordInfo = new SplitWordInfo();
        splitWordInfo.setPatentInfo(patentInfo);
        splitWordInfo.setSplitWordType(splitWordType);
        splitWordInfo.setWordInfoId(wordId);
        return splitWordInfo;
    }

    public List<String> createNGram(final int len, final String str) {
        final String[] parts = str.split(" ");
        final String[] result = new String[parts.length - len + 1];
        for (int i = 0; i < parts.length - len + 1; i++) {
            final StringBuilder sb = new StringBuilder();
            for (int k = 0; k < len; k++) {
                if (k > 0) {
                    sb.append(' ');
                }
                sb.append(parts[i + k].trim());
            }
            result[i] = sb.toString();
        }
        return new ArrayList<>(Arrays.asList(result));
    }

    private String removeMultipleSpaces(final String content) {

        String replacedContent = content;

        while (true) {

            if (replacedContent.contains("  ")) {
                replacedContent = replacedContent.replaceAll("  ", " ");
            } else {
                break;
            }
        }

        return replacedContent.trim();
    }

    private String removePunctuations(final String content) {
        return content.toLowerCase().replaceAll("[^a-zA-Z]", " ");
    }

    private String removeStopWords(final String content) {

        String replacedContent = content;

        final List<String> stopWordList = parserInitialRepository.getStopWordList();
        for (final String stopWord : stopWordList) {
            replacedContent = replacedContent.replaceAll(" ".concat(stopWord.trim()).concat(" "), " ");

            if (replacedContent.startsWith(stopWord.trim().concat(" "))) {
                replacedContent = replacedContent.replaceFirst(stopWord.trim().concat(" "), " ").trim();
            }
        }

        return replacedContent.trim();
    }
}