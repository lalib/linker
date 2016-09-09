package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

    @Override
    public List<BigDecimal> getTvWordIds() {
        return entityManager.createNativeQuery("select word_id from t_tv_info where result > 0.004").getResultList();
    }

    @Override
    public List<TvProcessInfo> getTfResult(final Long patentId, final Long lsrId, final Long patentCount) {

        final List<Object[]> resultList = entityManager.createNativeQuery("(SELECT KLM.WID, KLM.TF ,KLM.DF FROM ( " +
                "        SELECT W.ID AS WID,COUNT(S.C_WORD) AS TF,(SELECT COUNT(DISTINCT L.ID) " +
                "            FROM t_split_word Q " +
                "            INNER JOIN t_word_summary_info K ON K.C_WORD = Q.c_word " +
                "            INNER JOIN t_patent_info L ON L.ID = Q.c_patent_info_id " +
                "            INNER JOIN t_lsp_info LP ON LP.ID = L.c_lsp_id " +
                "            WHERE LP.c_lsr_id = :lsrId AND K.id = W.ID) AS DF " +
                "        FROM t_split_word S " +
                "        INNER JOIN t_word_summary_info W ON W.C_WORD = S.c_word " +
                "        INNER JOIN t_analyzable_word_info A ON A.c_word_id = W.ID " +
                "        INNER JOIN t_patent_info P ON P.ID = S.c_patent_info_id " +
                "        INNER JOIN t_tv_info TV ON TV.word_id = W.ID " +
                "        WHERE P.ID = :patentId AND TV.result >0.01 " +
                "        GROUP BY W.ID) AS KLM) " +
                "        UNION " +
                "        (SELECT KO.word_id,0,0 FROM t_tv_info KO WHERE word_id NOT IN ( " +
                "        SELECT TV.word_id " +
                "        FROM t_split_word S " +
                "        INNER JOIN t_word_summary_info W ON W.C_WORD = S.c_word" +
                "        INNER JOIN t_analyzable_word_info A ON A.c_word_id = W.ID " +
                "        INNER JOIN t_patent_info P ON P.ID = S.c_patent_info_id " +
                "        INNER JOIN t_tv_info TV ON TV.word_id = W.ID " +
                "        WHERE P.ID = :patentId AND TV.result >0.01" +
                "        GROUP BY TV.word_id)   AND KO.result >0.01)")
                .setParameter("lsrId", lsrId)
                .setParameter("patentId", patentId)
                .getResultList();

        final List<TvProcessInfo> tvProcessInfoList = new ArrayList<>();

        for (final Object[] objects : resultList) {

            final TvProcessInfo tvProcessInfo = new TvProcessInfo();
            tvProcessInfo.setWordId(((BigDecimal) objects[0]).longValue());
            tvProcessInfo.setPatentId(patentId);

            final Long tfValue = ((BigInteger) objects[1]).longValue();
            if (tfValue != 0L) {
                tvProcessInfo.setTfIdfValue(tfValue * Math.log(patentCount / ((BigInteger) objects[2]).longValue()));
            } else {
                tvProcessInfo.setTfIdfValue(0d);
            }
            tvProcessInfoList.add(tvProcessInfo);
        }

        return tvProcessInfoList;
    }

}