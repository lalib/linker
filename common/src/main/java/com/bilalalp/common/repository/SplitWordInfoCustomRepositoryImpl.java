package com.bilalalp.common.repository;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SplitWordInfoCustomRepositoryImpl implements SplitWordInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PatentWordCountDto> getPatentWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId) {

        return entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(s.patentInfo.id,s.patentInfo.version, COUNT(s.wordInfoId)) " +
                        "FROM SplitWordInfo s " +
                        "WHERE s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.wordInfoId =:word " +
                        "GROUP BY s.patentInfo.id,s.patentInfo.version")
                .setParameter("lsrId", linkSearchRequestInfo.getId())
                .setParameter("word", wordInfoId)
                .getResultList();
    }

    @Override
    public Long getPatentWordCountWithoutZeroCount(final LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId) {

        return (long) entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(s.patentInfo.id,s.patentInfo.version,COUNT(s.wordInfoId)) " +
                        "FROM SplitWordInfo s " +
                        "WHERE s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.wordInfoId =:word " +
                        "GROUP BY s.patentInfo.id,s.patentInfo.version " +
                        "HAVING COUNT(s.wordInfoId) > 0")
                .setParameter("lsrId", linkSearchRequestInfo.getId())
                .setParameter("word", wordInfoId)
                .getResultList().size();
    }

    @Override
    public Long getSplitWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId) {
        return (long) entityManager
                .createQuery("SELECT COUNT(DISTINCT p.id) FROM SplitWordInfo p " +
                        "WHERE p.patentInfo.linkSearchPageInfo.linkSearchRequestInfo = :linkSearchRequestInfo AND p.wordInfoId =:word")
                .setParameter("linkSearchRequestInfo", linkSearchRequestInfo)
                .setParameter("word", wordInfoId)
                .getSingleResult();
    }
}