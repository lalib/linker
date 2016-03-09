package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.AnalyzableWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface AnalyzableWordInfoService extends BaseService<AnalyzableWordInfo> {

    List<Long> getWordIds(TfIdfRequestInfo tfIdfRequestInfo, List<Long> idList);

    List<Long> getWordIds(TfIdfRequestInfo tfIdfRequestInfo);
}
