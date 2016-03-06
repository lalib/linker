package com.bilalalp.common.service;

import com.bilalalp.common.entity.tfidf.WordInfo;
import com.bilalalp.common.service.base.BaseService;

public interface WordInfoService extends BaseService<WordInfo> {

    Long insertInfoExists(String word);
}
