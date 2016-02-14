package com.bilalalp.parser.config;

import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.parser.service.ImporterService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Importer {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(ImporterService.class).importToQueue(4L);
    }
}