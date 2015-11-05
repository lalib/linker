package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchGeneratedLinkInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSearchGeneratedLinkInfoRepository extends CrudRepository<LinkSearchGeneratedLinkInfo, Long> {

}