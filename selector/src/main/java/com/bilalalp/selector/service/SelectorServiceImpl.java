package com.bilalalp.selector.service;

import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelectorServiceImpl implements SelectorService {

    @Transactional
    @Override
    public void selectKeyword(final KeywordSelectionRequest keywordSelectionRequest) {


    }
}