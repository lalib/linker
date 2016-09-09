package com.bilalalp.entropyinitializer;

import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.entropyinitializer.service.FileExporter;
import com.bilalalp.entropyinitializer.service.FileExporter2;
import com.bilalalp.entropyinitializer.service.TeamVarianceService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartUp {

    public static void main(String[] args) {
//        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(EntropyInitializerService.class).calculateEntropy();
//        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(TeamVarianceService.class).process();
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class, SpringMongoConfig.class).registerShutdownHook();
//        final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class, SpringMongoConfig.class);
//        annotationConfigApplicationContext.getBean(TeamVarianceService.class).mongoTest();
//        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class, SpringMongoConfig.class).getBean(TeamVarianceService.class).calculateTermVariance();
//        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class, SpringMongoConfig.class).getBean(FileExporter.class).exportFile();
    }
}
