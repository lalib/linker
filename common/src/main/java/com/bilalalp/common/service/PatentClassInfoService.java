package com.bilalalp.common.service;


import com.bilalalp.common.entity.patent.PatentClassInfo;
import com.bilalalp.common.service.base.BaseService;

public interface PatentClassInfoService extends BaseService<PatentClassInfo> {

    void deleteAllPatentInfoClasses(Long requestId);
}