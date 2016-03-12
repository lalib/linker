package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.service.base.BaseService;

public interface PatentRowInfoService extends BaseService<PatentRowInfo> {

    void saveInNewTransaction(PatentRowInfo patentRowInfo);
}
