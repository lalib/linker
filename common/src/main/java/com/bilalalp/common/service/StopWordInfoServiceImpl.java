package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.StopWordInfo;
import com.bilalalp.common.repository.StopWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
public class StopWordInfoServiceImpl extends AbstractService<StopWordInfo> implements StopWordInfoService {

    @Autowired
    private StopWordInfoRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void persistIfNotExist(final String word) {

        final StopWordInfo foundStopWord = repository.findByStopWord(word);

        if (foundStopWord == null) {
            final StopWordInfo stopWordInfo = new StopWordInfo();
            stopWordInfo.setStopWord(word);
            save(stopWordInfo);
        }
    }
}