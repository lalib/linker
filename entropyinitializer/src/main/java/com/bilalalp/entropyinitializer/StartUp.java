package com.bilalalp.entropyinitializer;

import com.bilalalp.common.config.CommonConfig;
import com.bilalalp.entropyinitializer.service.EntropyInitializerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartUp {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(EntropyInitializerService.class).calculateEntropy();
    }
}
