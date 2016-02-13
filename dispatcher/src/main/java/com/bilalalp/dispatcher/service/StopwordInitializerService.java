package com.bilalalp.dispatcher.service;

import com.bilalalp.common.service.StopWordInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Slf4j
@Service
public class StopWordInitializerService {

    @Autowired
    private StopWordInfoService stopWordInfoService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("stopwords.txt").getFile());

        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

            while ((line = reader.readLine()) != null) {
                stopWordInfoService.persistIfNotExist(line);
            }

        } catch (final IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}