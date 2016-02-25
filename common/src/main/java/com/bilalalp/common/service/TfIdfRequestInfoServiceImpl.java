package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.repository.TfIdfRequestInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TfIdfRequestInfoServiceImpl extends AbstractService<TfIdfRequestInfo> implements TfIdfRequestInfoService {

    @Autowired
    private TfIdfRequestInfoRepository repository;

}
