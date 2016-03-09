package com.bilalalp.tfidfprocessor.service;

import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import com.bilalalp.common.service.TfIdfInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
public class TfIdfFileService implements Serializable {

    @Autowired
    private TfIdfInfoService tfIdfInfoService;

    @Transactional
    public void process(final TfIdfProcessInfo tfIdfProcessInfo) {


    }
}
