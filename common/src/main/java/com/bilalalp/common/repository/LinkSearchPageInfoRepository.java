package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkSearchPageInfoRepository extends CrudRepository<LinkSearchPageInfo, Long> {

    @Query("SELECT p FROM LinkSearchPageInfo p where p.linkSearchRequestInfo.id = :id")
    List<LinkSearchPageInfo> getLinkSearchPageInfoListBylinkSearchRequestInfo(@Param("id") final Long id);
}