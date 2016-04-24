package com.bilalalp.clustering;

import com.bilalalp.clustering.config.QueueConfig;
import com.bilalalp.common.config.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.Serializable;

public class StartUp implements Serializable{

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).registerShutdownHook();
    }
}