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
    public List<PatentWordCountDto> getPatentWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, final String word) {

        return entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(s.patentInfo.id,COUNT(s.word)) FROM SplitWordInfo s WHERE s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.word =:word GROUP BY s.patentInfo.id")
                .setParameter("lsrId", linkSearchRequestInfo.getId())
                .setParameter("word", word)
                .getResultList();
    }

    @Override
    public Long getPatentWordCountWithoutZeroCount(final LinkSearchRequestInfo linkSearchRequestInfo, final String word) {

        return (long) entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(s.patentInfo.id,COUNT(s.word)) FROM SplitWordInfo s WHERE s.patentInfo.linkSearchPageInfo.linkSearchRequestInfo.id = :lsrId and s.word =:word GROUP BY s.patentInfo.id HAVING COUNT(s.word) > 0")
                .setParameter("lsrId", linkSearchRequestInfo.getId())
                .setParameter("word", word)
                .getResultList().size();
    }
}