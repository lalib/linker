package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.PatentClassInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PatentClassInfoRepository extends CrudRepository<PatentClassInfo, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM PatentClassInfo p WHERE p.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :requestId")
    void deleteAllPatentInfoClasses(@Param("requestId") Long requestId);
}