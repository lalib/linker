package com.bilalalp.common.service;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface LinkSearchPageInfoService extends BaseService<LinkSearchPageInfo> {

    List<LinkSearchPageInfo> getLinkSearchPageInfoListBylinkSearchRequestInfo(LinkSearchRequestInfo linkSearchRequestInfo);
}