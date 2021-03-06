package com.bilalalp.common.repository;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PatentInfoRepository extends JpaRepository<PatentInfo, Long> {

    @Transactional
    List<PatentInfo> getPatentListBylinkSearchPageInfo(LinkSearchPageInfo linkSearchPageInfo);

    @Query("SELECT p.id FROM PatentInfo p INNER JOIN p.linkSearchPageInfo l INNER JOIN l.linkSearchRequestInfo r WHERE r.id = :requestId")
    List<Long> getPatentIds(@Param("requestId") Long requestId);

    @Transactional
    @Modifying
    @Query("UPDATE PatentInfo p set p.abstractContent=null, p.applicationNumber=null,p.assignee=null,p.claimContent=null,p.descriptionContent=null,p.fillingDate=null,p.inventors=null,p.parsed=false,p.patentNumber=null,p.publicationDate=null WHERE p.linkSearchPageInfo.linkSearchRequestInfo.id= :requestId")
    void resetParseInformation(@Param("requestId") Long requestId);

    @Query("SELECT COUNT(p.id) FROM PatentInfo p WHERE p.linkSearchPageInfo.linkSearchRequestInfo = :linkSearchRequestInfo")
    Long getPatentInfoCountByLinkSearchRequestInfo(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo);

    @Query("SELECT DISTINCT new com.bilalalp.common.dto.EntityDto(s.id,s.version) " +
            "FROM PatentInfo s WHERE s.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.id NOT IN " +
            "(SELECT k.patentInfo.id FROM SplitWordInfo k,WordSummaryInfo w WHERE k.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and w.word=k.word AND w.id =:word)")
    List<EntityDto> getPatentInfoIds(@Param("lsrId") Long lsrId, @Param("word") Long word);

    @Query("SELECT DISTINCT p.id FROM SplitWordInfo s, PatentInfo p " +
            "INNER JOIN p.linkSearchPageInfo l " +
            "INNER JOIN l.linkSearchRequestInfo r " +
            "WHERE r.id = :lsrId AND s.patentInfo = p AND s.word = :word")
    List<Long> getPatentIds(@Param("lsrId") Long lsrId, @Param("word") String word);

    @Query("select p FROM PatentInfo p")
    List<Long> getPatentIds(Pageable pageable);


    @Query("select p.id from PatentInfo p where p.fillingDate is not null and p.fillingDate > :fillingDate")
    List<Long> getPatentIdsByDate(@Param("fillingDate") Date date);
}