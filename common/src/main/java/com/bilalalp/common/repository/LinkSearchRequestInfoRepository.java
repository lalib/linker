package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkSearchRequestInfoRepository extends CrudRepository<LinkSearchRequestInfo, Long> {
}