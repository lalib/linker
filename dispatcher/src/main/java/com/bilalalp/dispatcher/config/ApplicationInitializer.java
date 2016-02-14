package com.bilalalp.dispatcher.config;

import com.bilalalp.dispatcher.service.StopWordInitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Service
public class ApplicationInitializer implements Serializable {

    @Autowired
    private StopWordInitializerService stopWordInitializerService;

    @PostConstruct
    public void init() {
        stopWordInitializerService.init();
    }
}