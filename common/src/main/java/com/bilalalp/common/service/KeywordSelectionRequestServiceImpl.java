package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import com.bilalalp.common.repository.KeywordSelectionRequestRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Getter
@Service
public class KeywordSelectionRequestServiceImpl extends AbstractService<KeywordSelectionRequest> implements KeywordSelectionRequestService {

    @Autowired
    private KeywordSelectionRequestRepository repository;

}