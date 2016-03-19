package com.bilalalp.clusteranalyzerprocessor;

import com.bilalalp.clusteranalyzerprocessor.config.QueueConfig;
import com.bilalalp.common.config.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartUp {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).registerShutdownHook();
    }
}