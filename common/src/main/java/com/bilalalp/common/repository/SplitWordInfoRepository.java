package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.SplitWordInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SplitWordInfoRepository extends CrudRepository<SplitWordInfo, Long> {

    @Transactional
    @Modifying
    @Query("delete from SplitWordInfo s where s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id= :requestId")
    void deleteByRequestId(@Param("requestId") Long requestId);
}