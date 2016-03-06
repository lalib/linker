package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.WordInfo;
import com.bilalalp.common.repository.WordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
public class WordInfoServiceImpl extends AbstractService<WordInfo> implements WordInfoService {

    @Autowired
    private WordInfoRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Long insertInfoExists(final String word) {

        final WordInfo foundWordInfo = repository.findByWord(word);
        if (foundWordInfo == null) {
            final WordInfo wordInfo = new WordInfo();
            wordInfo.setWord(word);
            save(wordInfo);
            return wordInfo.getId();
        }
        return foundWordInfo.getId();
    }
}
