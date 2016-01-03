package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SelectedKeywordInfo;
import com.bilalalp.common.repository.SelectedKeywordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class SelectedKeywordInfoServiceImpl extends AbstractService<SelectedKeywordInfo> implements SelectedKeywordInfoService {

    @Autowired
    private SelectedKeywordInfoRepository selectedKeywordInfoRepository;

    @Override
    protected CrudRepository<SelectedKeywordInfo, Long> getRepository() {
        return selectedKeywordInfoRepository;
    }
}