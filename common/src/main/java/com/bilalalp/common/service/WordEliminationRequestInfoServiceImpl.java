package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.WordEliminationRequestInfo;
import com.bilalalp.common.repository.WordEliminationRequestInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class WordEliminationRequestInfoServiceImpl extends AbstractService<WordEliminationRequestInfo> implements WordEliminationRequestInfoService {

    @Autowired
    private WordEliminationRequestInfoRepository repository;
}