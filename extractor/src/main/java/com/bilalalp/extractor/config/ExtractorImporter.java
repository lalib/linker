package com.bilalalp.extractor.config;

import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.extractor.service.ImporterService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ExtractorImporter {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class);
        annotationConfigApplicationContext.getBean(ImporterService.class).importToQueue(4L);
    }
}