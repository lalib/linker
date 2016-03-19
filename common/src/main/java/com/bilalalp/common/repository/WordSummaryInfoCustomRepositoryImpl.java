package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class WordSummaryInfoCustomRepositoryImpl implements WordSummaryInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo) {

        final String query = "INSERT INTO T_WORD_SUMMARY_INFO(id,c_version,c_word,c_count,c_lsr_id)" +
                "                               SELECT (nextval('hibernate_sequence')),0,t.C_WORD,count(t.C_WORD),? " +
                "                                FROM t_split_word t " +
                "                               INNER JOIN T_PATENT_INFO k ON k.id = t.C_PATENT_INFO_ID " +
                "                              INNER JOIN T_LSP_INFO p ON p.id = k.C_LSP_ID " +
                "                              INNER JOIN T_LSR_INFO R ON R.id = p.C_LSR_ID  " +
                "                              WHERE R.id= ? " +
                "                             GROUP BY t.C_WORD " +
                "                             ORDER BY count(t.C_WORD) DESC ";

        entityManager.createNativeQuery(query)
                .setParameter(1, linkSearchRequestInfo.getId())
                .setParameter(2, linkSearchRequestInfo.getId())
                .executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Long> getWordIds(final Long lsrId, final List<String> wordList) {
        return entityManager
                .createQuery("SELECT p.id FROM WordSummaryInfo p " +
                        "WHERE p.linkSearchRequestInfo.id = :lstId AND p.word IN :wordList")
                .setParameter("lstId", lsrId)
                .setParameter("wordList", wordList)
                .getResultList();
    }
}