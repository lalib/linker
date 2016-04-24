package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SelectedKeywordInfo;
import com.bilalalp.common.repository.SelectedKeywordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Getter
@Service
public class SelectedKeywordInfoServiceImpl extends AbstractService<SelectedKeywordInfo> implements SelectedKeywordInfoService {

    @Autowired
    private SelectedKeywordInfoRepository repository;
}