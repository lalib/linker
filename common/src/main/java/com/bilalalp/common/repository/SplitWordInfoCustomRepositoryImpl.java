package com.bilalalp.common.repository;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
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
    public List<BigInteger> getWords(Long lsrId) {
        return entityManager.createNativeQuery("select w.id from t_split_word s " +
                "inner join t_patent_info  p on p.id = s.c_patent_info_id " +
                "inner join t_lsp_info lp on lp.id = p.c_lsp_id " +
                "inner join t_lsr_info r on r.id = lp.c_lsr_id " +
                "inner join t_word_summary_info w on w.c_word = s.c_word " +
                "where r.id = :lsrId " +
                "group by w.id " +
                "having count(s.c_word) >20 and count(s.c_word)<2000 " +
                "order by count(s.c_word) desc").setParameter("lsrId", lsrId).getResultList();
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

    @Override
    public List<String> getWordIdsByClusterIdAndLimit(final Long clusteringRequestId, final Long clusterNumber, final Long wordLimit) {

        Query query = entityManager
                .createQuery("SELECT s.word FROM SplitWordInfo s ,PatentInfo p, ClusteringRequestInfo c, ClusteringResultInfo cr " +
                        "WHERE s.patentInfo.id = p.id AND c.id = cr.clusteringRequestId and cr.patentId = p.id AND c.id = :clusteringRequestId AND cr.clusteringNumber = :clusterNumber " +
                        "GROUP BY s.word " +
                        "ORDER BY COUNT(s.word) DESC");

        if (wordLimit != null) {
            query = query.setMaxResults(wordLimit.intValue());
        }
        return query.setParameter("clusteringRequestId", clusteringRequestId).setParameter("clusterNumber", clusterNumber).getResultList();
    }

    @Override
    public Long getWordCountInACluster(final Long clusterNumber, final Long clusterRequestId, final Long wordId) {

        return (Long) entityManager.createQuery("SELECT COUNT(s.word) FROM SplitWordInfo s, PatentInfo p, ClusteringRequestInfo c, ClusteringResultInfo cr, WordSummaryInfo w " +
                "WHERE s.patentInfo.id = p.id AND c.id = cr.clusteringRequestId AND cr.patentId = p.id AND " +
                "c.id = :clusteringRequestId AND cr.clusteringNumber =:clusteringNumber AND w.id = :wordId AND w.word = s.word")
                .setParameter("clusteringNumber", clusterNumber)
                .setParameter("clusteringRequestId", clusterRequestId)
                .setParameter("wordId", wordId)
                .getSingleResult();
    }

    @Override
    public Long getTotalPatentCountInOtherClusters(final Long clusterNumber, final Long clusterRequestId, final Long wordId) {

        return (Long) entityManager.createQuery("SELECT COUNT(DISTINCT p.id) FROM SplitWordInfo s, PatentInfo p, ClusteringRequestInfo c, ClusteringResultInfo cr, WordSummaryInfo w " +
                "where s.patentInfo.id = p.id AND c.id = cr.clusteringRequestId AND cr.patentId = p.id AND c.id = :clusteringRequestId " +
                "AND cr.clusteringNumber != :clusteringNumber AND w.id = :wordId AND w.word = s.word ")
                .setParameter("clusteringRequestId", clusterRequestId)
                .setParameter("clusteringNumber", clusterNumber)
                .setParameter("wordId", wordId).getSingleResult();
    }
}