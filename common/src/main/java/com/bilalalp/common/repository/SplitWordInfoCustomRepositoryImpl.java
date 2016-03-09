package com.bilalalp.common.repository;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
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
    public List<PatentWordCountDto> getWordCount(final Long patentId) {
        return entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(w.id,w.version, COUNT(s.word)) " +
                        "FROM SplitWordInfo s " +
                        "INNER JOIN WordSummaryInfo w ON w.word = s.word " +
                        "INNER JOIN AnalyzableWordInfo a ON a.wordId = w.id " +
                        "WHERE s.patentInfo.id = :patentId " +
                        "GROUP BY w.id,w.version " +
                        "ORDER BY COUNT(s.word) DESC")
                .setParameter("patentId", patentId)
                .getResultList();
    }

    @Override
    public List<Long> getExceptedWordIdList(final TfIdfRequestInfo tfIdfRequestInfo, final List<Long> wordIds) {

        return entityManager
                .createQuery("SELECT p FROM WordSummaryInfo p " +
                        "INNER JOIN AnalyzableWordInfo a ON a.wordId = p.id " +
                        "WHERE p.id NOT in :wordIds AND a.tfIdfRequestInfo = :tfIdfRequestInfo")
                .setParameter("tfIdfRequestInfo", tfIdfRequestInfo)
                .setParameter("wordIds", wordIds)
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