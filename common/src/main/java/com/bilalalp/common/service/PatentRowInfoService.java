package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.service.base.BaseService;

import java.io.Serializable;

public interface PatentRowInfoService extends BaseService<PatentRowInfo>, Serializable {

    void saveInNewTransaction(PatentRowInfo patentRowInfo);
}
