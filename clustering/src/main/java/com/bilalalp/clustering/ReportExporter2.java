package com.bilalalp.clustering;

import com.bilalalp.clustering.config.QueueConfig;
import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.common.service.PatentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ReportExporter2 {

    @Autowired
    private PatentInfoService patentInfoService;

    public static void main(String[] args) {
        final ReportExporter2 bean = new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(ReportExporter2.class);
        System.out.println(bean.getPatentCount(23,10));
    }

    private String getPatentCount(final long id, final long totalClusterCount) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (long i = 1; i < totalClusterCount + 1; i++) {
            stringBuilder.append("Cluster ").append(i).append(" ").append(patentInfoService.getPatentCount(id, i)).append("\n");
        }

        return stringBuilder.toString();
    }


}
