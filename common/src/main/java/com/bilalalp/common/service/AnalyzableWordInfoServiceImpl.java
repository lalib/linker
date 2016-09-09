package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.AnalyzableWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.repository.AnalyzableWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Service
public class AnalyzableWordInfoServiceImpl extends AbstractService<AnalyzableWordInfo> implements AnalyzableWordInfoService {

    @Autowired
    private AnalyzableWordInfoRepository repository;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Long> getWordIds(TfIdfRequestInfo tfIdfRequestInfo, List<Long> idList,List<Long> inList) {
        return repository.getWordIds(tfIdfRequestInfo, idList,inList);
    }

    @Override
    public List<Long> getWordIds(TfIdfRequestInfo tfIdfRequestInfo) {
        return repository.getWordIds(tfIdfRequestInfo);
    }

    @Override
    public List<Long> getWordIds(TfIdfRequestInfo tfIdfRequestInfo,List<Long> idList) {
        return repository.getWordIds(tfIdfRequestInfo,idList);
    }
}
