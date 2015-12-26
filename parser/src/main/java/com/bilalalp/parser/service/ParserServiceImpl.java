package com.bilalalp.parser.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.SplitWordInfo;
import com.bilalalp.common.entity.SplitWordType;
import com.bilalalp.common.service.SplitWordInfoService;
import com.bilalalp.parser.config.ParserInitialRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private ParserInitialRepository parserInitialRepository;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Override
    public void parse(final PatentInfo patentInfo) {

        final String abstractContent = patentInfo.getAbstractContent();
        if (StringUtils.isNotEmpty(abstractContent)) {
            final String removedPunctuations = removePunctuations(abstractContent);
            final String removedStopWords = removeStopWords(removedPunctuations);
            final String removedMultipleSpaces = removeMultipleSpaces(removedStopWords);
            if(StringUtils.isEmpty(removedMultipleSpaces.trim())){
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

        final List<SplitWordInfo> splitWordInfoList = new ArrayList<>();
        for (final String word : splitBySpaceList) {
            final SplitWordInfo splitWordInfo = new SplitWordInfo();
            splitWordInfo.setPatentInfo(patentInfo);
            splitWordInfo.setSplitWordType(splitWordType);
            splitWordInfo.setWord(word);
            splitWordInfoList.add(splitWordInfo);
        }

        return splitWordInfoList;
    }

    public List<String> createNGram(final int len, final String str) {
        final String[] parts = str.split(" ");
        final String[] result = new String[parts.length - len + 1];
        for (int i = 0; i < parts.length - len + 1; i++) {
            final StringBuilder sb = new StringBuilder();
            for (int k = 0; k < len; k++) {
                if (k > 0) sb.append(' ');
                sb.append(parts[i + k]);
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

        return replacedContent;
    }

    private String removePunctuations(final String content) {
        return content.toLowerCase().replaceAll("[^a-zA-Z]", " ");
    }

    private String removeStopWords(final String content) {

        String replacedContent = content;

        for (final String stopWord : parserInitialRepository.getStopWordList()) {
            replacedContent = replacedContent.replaceAll(stopWord, " ");
        }

        return replacedContent;
    }
}