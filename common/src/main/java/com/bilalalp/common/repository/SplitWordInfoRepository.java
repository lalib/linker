package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SplitWordInfoRepository extends JpaRepository<SplitWordInfo, Long> {

    @Transactional
    @Modifying
    @Query("delete from SplitWordInfo s where s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id= :requestId")
    void deleteByRequestId(@Param("requestId") Long requestId);

    @Query("SELECT COUNT(p.id) FROM SplitWordInfo p,WordSummaryInfo w WHERE p.patentInfo.id = :patentInfo AND w.id=:wordInfoId AND p.word = w.word")
    Long getCountByPatentInfoIdAndWord(@Param("patentInfo") Long patentInfoId, @Param("wordInfoId") Long wordInfoId);

    @Query("SELECT COUNT(DISTINCT p.patentInfo.id) FROM SplitWordInfo p, WordSummaryInfo w " +
            "WHERE p.patentInfo.linkSearchPageInfo.linkSearchRequestInfo =:linkSearchRequestInfo AND p.word = w.word AND w.id = :wordInfoId")
    Long getWordCountByLinkSearchRequestInfoAndWord(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, @Param("wordInfoId") Long wordInfoId);
}