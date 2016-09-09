package com.bilalalp.clustering;


import com.bilalalp.clustering.config.QueueConfig;
import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.common.service.PatentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ReportExporter {

    @Autowired
    private PatentInfoService patentInfoService;

    public static void main(String[] args) {
        final ReportExporter bean = new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(ReportExporter.class);
        final String s = bean.exportValues(24L, 5L);
        System.out.println(s);
    }

    public String exportValues(final Long id, final Long totalCluster) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < totalCluster + 1; i++) {
            for (int k = i + 1; k < totalCluster + 1; k++) {

                final BigInteger mutualWordCount = patentInfoService.getMutualWordCount(id, (long) i, (long) k);

                stringBuilder.append(i).append(" ").append(k).append(" ").append(mutualWordCount).append("\n");
            }
        }

        return stringBuilder.toString();
    }
}