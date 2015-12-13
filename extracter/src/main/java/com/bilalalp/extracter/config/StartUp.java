package com.bilalalp.extracter.config;

import com.bilalalp.common.config.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartUp {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).registerShutdownHook();
    }
}