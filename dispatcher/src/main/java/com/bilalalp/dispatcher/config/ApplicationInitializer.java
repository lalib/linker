package com.bilalalp.dispatcher.config;

import com.bilalalp.dispatcher.service.WordInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Service
public class ApplicationInitializer implements Serializable {

    @Autowired
    private WordInitializerService wordInitializerService;

    @PostConstruct
    public void init() {
        wordInitializerService.init();
    }
}