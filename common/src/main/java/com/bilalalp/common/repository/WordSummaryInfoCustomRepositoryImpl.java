package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class WordSummaryInfoCustomRepositoryImpl implements WordSummaryInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void bulkInsert(final LinkSearchRequestInfo linkSearchRequestInfo) {

        final String query = "INSERT INTO T_WORD_SUMMARY_INFO(id,c_version,c_word,c_count,c_lsr_id) " +
                "                SELECT (nextval('hibernate_sequence')),0,t.c_word, count(t.c_word), ? " +
                "                FROM t_split_word t " +
                "                INNER JOIN T_PATENT_INFO k ON k.id = t.C_PATENT_INFO_ID " +
                "                INNER JOIN T_LSP_INFO p ON p.id = k.C_LSP_ID " +
                "                INNER JOIN T_LSR_INFO R ON R.id = p.C_LSR_ID " +
                "                WHERE R.id= ? " +
                "               GROUP BY t.c_word " +
                "               ORDER BY count(t.c_word) DESC";

        entityManager.createNativeQuery(query)
                .setParameter(1, linkSearchRequestInfo.getId())
                .setParameter(2, linkSearchRequestInfo.getId())
                .executeUpdate();
    }
}