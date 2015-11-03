package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSearchPageInfoRepository extends CrudRepository<LinkSearchPageInfo, Long> {
}