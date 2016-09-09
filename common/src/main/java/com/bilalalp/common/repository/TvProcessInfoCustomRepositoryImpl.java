package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TvProcessInfoCustomRepositoryImpl implements TvProcessInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TvProcessInfo> findByLimitWithoutAdditionalWords(final Long count) {

        return entityManager.createNativeQuery("select tv.* from T_TV_PROCESS_INF tv " +
                "inner join t_word_summary_info s on s.id = tv.wordid " +
                "where s.c_word not in (select a.c_word from t_add_word_info a) " +
                "order by tv.tfidfvalue desc " +
                "limit :limitcount", TvProcessInfo.class).setParameter("limitcount", count).getResultList();
    }

    @Override
    public List<TvProcessInfo> findByLimit(Long count, List<Long> patentIds) {


        final javax.persistence.Query nativeQuery = entityManager.createNativeQuery("SELECT TV.* FROM T_TV_PROCESS_INF TV, t_word_summary_info W, t_patent_info I, t_split_word S " +
                "WHERE W.ID = TV.wordid AND I.ID IN (" + getPatentIds(patentIds) + ") AND S.C_PATENT_INFO_ID = I.ID AND W.C_WORD NOT IN (SELECT k.c_word FROM  T_ADD_WORD_INFO k) ORDER BY TV.TFIDFVALUE DESC LIMIT :limitcount", TvProcessInfo.class);


        return nativeQuery.setParameter("limitcount", count).getResultList();
    }

    private String getPatentIds(final List<Long> patentIds) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < patentIds.size(); i++) {

            stringBuilder.append(patentIds.get(i));
            if (i != patentIds.size() - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }
}
