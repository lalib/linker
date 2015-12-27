package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkSearchPageInfoRepository extends CrudRepository<LinkSearchPageInfo, Long> {

    List<LinkSearchPageInfo> getLinkSearchPageInfoListBylinkSearchRequestInfo(LinkSearchRequestInfo linkSearchRequestInfo);
}