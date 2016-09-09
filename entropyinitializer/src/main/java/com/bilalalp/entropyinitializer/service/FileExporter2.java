package com.bilalalp.entropyinitializer.service;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.service.SplitWordInfoService;
import com.bilalalp.common.service.TvProcessInfoService;
import com.bilalalp.common.service.WordSummaryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class FileExporter2 implements Serializable {

    @Autowired
    private TvProcessInfoService tvProcessInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    public void exportFile() {

        final String networkFileName = "C:\\bilal\\networkFile.txt";
        final String mapFileName = "C:\\bilal\\mapFile.txt";

        final StringBuilder stringBuilder = new StringBuilder();

        final List<TvProcessInfo> tvProcessInfoList = tvProcessInfoService.findByLimit(3000);

        Long counter = 0L;

        final StringBuilder mapBuilder = new StringBuilder();
        mapBuilder.append("id\tlabel\n");

        for (final TvProcessInfo firstTvProcessInfo : tvProcessInfoList) {

            final WordSummaryInfo firstWordSummaryInfo = wordSummaryInfoService.find(firstTvProcessInfo.getWordId());
            stringBuilder.append(firstWordSummaryInfo.getId()).append("\t");
            mapBuilder.append(firstWordSummaryInfo.getId()).append("\t").append(firstWordSummaryInfo.getWord()).append("\n");
            counter++;
            System.out.println(counter);

            final Map<String, BigInteger> excludedMutualWordCountMap = splitWordInfoService.getExcludedMutualWordCountMap(firstWordSummaryInfo.getWord(), 3000L);

            for (final Map.Entry<String, BigInteger> stringBigIntegerMap : excludedMutualWordCountMap.entrySet()) {
                stringBuilder.append(stringBigIntegerMap.getValue().longValue()).append("\t");
            }

            stringBuilder.append("\n");
        }

        writeToFile(mapFileName, mapBuilder.toString());
        writeToFile(networkFileName, stringBuilder.toString());
    }

    private void writeToFile(final String fileName, final String content) {
        try (PrintWriter pw = new PrintWriter(fileName, "UTF-8")) {
            pw.println(content);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}