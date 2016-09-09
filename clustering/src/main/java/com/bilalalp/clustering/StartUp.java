package com.bilalalp.clustering;

import com.bilalalp.clustering.config.QueueConfig;
import com.bilalalp.common.config.CommonConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.Serializable;

public class StartUp implements Serializable {

    public static void main(String[] args) throws IOException {
        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).registerShutdownHook();
//        new AnnotationConfigApplicationContext(QueueConfig.class, CommonConfig.class).getBean(FileTransformer2.class).createFile();
    }

}