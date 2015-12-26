package com.bilalalp.common.repository;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PatentInfoRepository extends CrudRepository<PatentInfo, Long> {

    @Transactional
    List<PatentInfo> getPatentListBylinkSearchPageInfo(LinkSearchPageInfo linkSearchPageInfo);
}