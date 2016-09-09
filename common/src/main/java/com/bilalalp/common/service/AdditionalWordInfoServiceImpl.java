package com.bilalalp.common.service;

import com.bilalalp.common.entity.AdditionalWordInfo;
import com.bilalalp.common.repository.AdditionalWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import com.bilalalp.common.service.base.BaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;

@Slf4j
@Getter
@Service
public class AdditionalWordInfoServiceImpl extends AbstractService<AdditionalWordInfo> implements BaseService<AdditionalWordInfo> {

    @Autowired
    private AdditionalWordInfoRepository repository;

//    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("additionalwords.txt").getFile());

        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

            while ((line = reader.readLine()) != null) {
                persistIfNotExist(line);
            }

        } catch (final IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void persistIfNotExist(final String word) {

        final AdditionalWordInfo foundStopWord = repository.findByWord(word);

        if (foundStopWord == null) {
            final AdditionalWordInfo stopWordInfo = new AdditionalWordInfo();
            stopWordInfo.setWord(word);
            save(stopWordInfo);
        }
    }
}
