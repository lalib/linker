package com.bilalalp.exporter.config;

import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.exporter.service.ExporterService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class StartUp {

    public static void main(String[] args) throws IOException {
        final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ExporterConfig.class, CommonConfig.class);
        final ExporterService exporterService = annotationConfigApplicationContext.getBean(ExporterService.class);
        exporterService.export(12658L);
    }
}
