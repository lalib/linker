package com.bilalalp.common.service;

import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface PatentInfoService extends BaseService<PatentInfo> {

    List<PatentInfo> getPatentListBylinkSearchPageInfo(LinkSearchPageInfo linkSearchPageInfo);
}