package com.bilalalp.parser.config;

import com.bilalalp.common.entity.patent.StopWordInfo;
import com.bilalalp.common.service.StopWordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParserRepositoryInitializer {

    @Autowired
    private StopWordInfoService stopWordInfoService;

    @Autowired
    private ParserInitialRepository parserInitialRepository;

    @PostConstruct
    public void init() {
        final List<String> stopWords = initStopWords();
        parserInitialRepository.setStopWordList(stopWords);
    }

    private List<String> initPunctuations() {
        return Arrays.asList("", "");
    }

    private List<String> initStopWords() {
        final List<StopWordInfo> stopWordInfoList = stopWordInfoService.findAll();
        return stopWordInfoList
                .stream().map(k -> " ".concat(k.getStopWord()).concat(" "))
                .collect(Collectors.toList());
    }
}