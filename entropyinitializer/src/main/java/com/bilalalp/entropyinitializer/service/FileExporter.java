package com.bilalalp.entropyinitializer.service;

import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.service.SplitWordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FileExporter implements Serializable {

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    public void exportFile() {

        final String networkFileName = "C:\\bilal\\networkFile.txt";
        final String mapFileName = "C:\\bilal\\mapFile.txt";


        final StringBuilder mapBuilder = new StringBuilder();

        mapBuilder.append("id\tlabel\n");

        final Long patentCount = 3000L;

        final List<Long> all = patentInfoService.getPatentIdsWithLimit(patentCount);

        Long counter = 0L;

        final StringBuilder stringBuilder = new StringBuilder();

        for (Long patentInfo : all) {

            final List<String> topWords = splitWordInfoService.getTopWords(patentInfo);
            mapBuilder.append(patentInfo).append("\t").append(getFormattedWords(topWords)).append("\n");

            final Map<BigInteger, BigInteger> excludedMutualPatentCountMap = splitWordInfoService.getPatentValues(patentInfo, patentCount);

//            final Long avgValue = findAverage(excludedMutualPatentCountMap);
//            final BigInteger maxValue = findMaxValue(excludedMutualPatentCountMap);

//            System.out.println(maxValue);
            for (final Map.Entry<BigInteger, BigInteger> stringBigIntegerMap : excludedMutualPatentCountMap.entrySet()) {
                stringBuilder.append(patentInfo).append("\t").append(stringBigIntegerMap.getKey().longValue()).append("\t");

//                if (70L > stringBigIntegerMap.getValue().longValue()) {
                    stringBuilder.append(stringBigIntegerMap.getValue().longValue()).append("\n");
//                } else {
//                    stringBuilder.append(0L).append("\n");
//                }
            }

            counter++;
            System.out.println(counter);

        }

        writeToFile(mapFileName, mapBuilder.toString());
        writeToFile(networkFileName, stringBuilder.toString());
    }

    private BigInteger findMaxValue(Map<BigInteger, BigInteger> excludedMutualPatentCountMap) {
         BigInteger maxValue = BigInteger.valueOf(0);

        for(Map.Entry<BigInteger,BigInteger> bigIntegerBigIntegerEntry : excludedMutualPatentCountMap.entrySet()){
            if(maxValue.longValue() < bigIntegerBigIntegerEntry.getValue().longValue()){
                maxValue = bigIntegerBigIntegerEntry.getValue();
            }
        }
        return maxValue;
    }

    private Long findAverage(Map<BigInteger, BigInteger> excludedMutualPatentCountMap) {

        Long avg = 0L;

        for (BigInteger val : excludedMutualPatentCountMap.values()) {
            avg += val.longValue();
        }
        return avg / excludedMutualPatentCountMap.values().size();
    }

    private String getFormattedWords(List<String> topWords) {

        final StringBuilder stringBuilder = new StringBuilder();

        int counter = 0;
        for (final String word : topWords) {

            counter++;
            stringBuilder.append(word);

            if (counter != 3) {
                stringBuilder.append(" - ");
            }
        }

        return stringBuilder.toString().trim();
    }

    private void writeToFile(final String fileName, final String content) {

        try {
            Files.write(Paths.get(fileName), Collections.singletonList(content.trim()), Charset.forName("UTF-8"), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}