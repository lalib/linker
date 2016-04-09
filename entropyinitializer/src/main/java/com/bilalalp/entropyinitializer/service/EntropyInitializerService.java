package com.bilalalp.entropyinitializer.service;

import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.service.SplitWordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EntropyInitializerService {

    @Autowired
    private SplitWordInfoService splitWordInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    public void calculateEntropy() {

        final List<String> words = Arrays.asList("datum", "grid computing", "cloud computing", "mobile device", "information","paas","iaas","saas","cloud manager");
        final long requestId = 574L;

        for (final String word : words) {

            final List<Long> patentIds = patentInfoService.getPatentIds(requestId);
            final List<Long> patentIdList = patentInfoService.getPatentIds(requestId, word);

            final Long totalPatentCount = (long) patentIds.size();
            final Long avaiablePatentCount = (long) patentIdList.size();
            final Long remainingPatentCount = totalPatentCount - avaiablePatentCount;

            final double firstOne = avaiablePatentCount.doubleValue() / totalPatentCount.doubleValue();
            final Double firstLeftOne = firstOne * (Math.log(firstOne) / Math.log(2d));

            final double secondOne = remainingPatentCount.doubleValue() / totalPatentCount.doubleValue();
            final Double secondLeftOne = secondOne * (Math.log(secondOne) / Math.log(2d));

            final Double entropy = firstLeftOne - secondLeftOne;
            System.out.println(word +" ::: " + entropy);
        }
    }

    public static void madsin(String[] args) {
        System.out.println(Double.valueOf("-0.1264037952130076") > Double.valueOf("-0.005965999540257678"));
    }
}