package com.bilalalp.common.repository;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PatentInfoRepository extends CrudRepository<PatentInfo, Long> {

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

    @Query("SELECT DISTINCT new com.bilalalp.common.dto.EntityDto(s.patentInfo.id,s.patentInfo.version) FROM SplitWordInfo s WHERE s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.patentInfo.id NOT IN (SELECT k.patentInfo.id FROM SplitWordInfo k WHERE k.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and k.word =:word)")
    List<EntityDto> getPatentInfoIds(@Param("lsrId") Long lsrId, @Param("word") String word);
}