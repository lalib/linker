package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import com.bilalalp.common.repository.TfIdfProcessInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TfIdfProcessInfoServiceImpl extends AbstractService<TfIdfProcessInfo> implements TfIdfProcessInfoService {

    @Autowired
    private TfIdfProcessInfoRepository repository;
}