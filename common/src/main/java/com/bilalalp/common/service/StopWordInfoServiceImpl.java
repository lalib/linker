package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.StopWordInfo;
import com.bilalalp.common.repository.StopWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StopWordInfoServiceImpl extends AbstractService<StopWordInfo> implements StopWordInfoService {

    @Autowired
    private StopWordInfoRepository stopWordInfoRepository;

    @Override
    protected CrudRepository<StopWordInfo, Long> getRepository() {
        return stopWordInfoRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void persistIfNotExist(final String word) {

        final StopWordInfo foundStopWord = stopWordInfoRepository.findByStopWord(word);

        if (foundStopWord == null) {
            final StopWordInfo stopWordInfo = new StopWordInfo();
            stopWordInfo.setStopWord(word);
            save(stopWordInfo);
        }
    }
}