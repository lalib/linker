package com.bilalalp.clustering;

import com.bilalalp.clustering.config.ClusteringConfig;
import com.bilalalp.clustering.service.KmeansClusterService;
import com.bilalalp.clustering.service.PatentFileRowMapperService;
import com.bilalalp.common.config.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartUp {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ClusteringConfig.class, CommonConfig.class);
        annotationConfigApplicationContext.getBean(PatentFileRowMapperService.class).insertPatentRowsToDatabase();
        annotationConfigApplicationContext.getBean(KmeansClusterService.class).cluster();
        annotationConfigApplicationContext.registerShutdownHook();
    }
}