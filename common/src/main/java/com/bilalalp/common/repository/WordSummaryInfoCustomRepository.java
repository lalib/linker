package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;

public interface WordSummaryInfoCustomRepository {

    void bulkInsert(LinkSearchRequestInfo linkSearchRequestInfo);
}