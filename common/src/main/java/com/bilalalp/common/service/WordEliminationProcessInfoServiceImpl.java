package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.WordEliminationProcessInfo;
import com.bilalalp.common.repository.WordEliminationProcessInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class WordEliminationProcessInfoServiceImpl extends AbstractService<WordEliminationProcessInfo> implements WordEliminationProcessInfoService {

    @Autowired
    private WordEliminationProcessInfoRepository repository;
}
