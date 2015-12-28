package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.service.base.BaseService;

public interface SplitWordInfoService extends BaseService<SplitWordInfo> {

    void deleteByRequestId(Long requestId);
}