package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestKeywordInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSearchRequestKeywordInfoRepository extends CrudRepository<LinkSearchRequestKeywordInfo, Long> {

}