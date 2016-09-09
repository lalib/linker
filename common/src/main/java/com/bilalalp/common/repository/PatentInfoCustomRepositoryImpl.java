package com.bilalalp.common.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PatentInfoCustomRepositoryImpl implements PatentInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Long> getPatentIds(Long limit) {

        final List mlimit = entityManager.createNativeQuery("select p.id from t_patent_info p " +
                "where p.c_filling_date is not null order by p.c_filling_date desc limit :mlimit")
                .setParameter("mlimit", limit)
                .getResultList();

        return (List<Long>) mlimit.stream().map(o -> ((BigInteger) o).longValue())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Long> getPatentRelationMap(Long patentId, List<Long> patentIds) {

        final Map<Long, Long> patentRelationMap = new HashMap<>();

        final List patentid = entityManager.createNativeQuery("with m1(patentid,mcount) as " +
                "(select p1.id,count(distinct(s.c_word)) from t_patent_info p1 " +
                "inner join t_split_word s on s.c_patent_info_id = p1.id " +
                "where s.c_word in " +
                "(select distinct(k.c_word) from t_patent_info p2 " +
                "inner join t_split_word k on k.c_patent_info_id = p2.id " +
                "where p2.id = :patentid) and p1.id in :patentidlist " +
                "group by p1.id), " +
                "m2(patentid,mcount) as " +
                "(select p.id,0 from t_patent_info p where p.id not in (select k.patentid from m1 k) and p.id in :patentidlist ) " +
                ", " +
                "m3(patentid,mcount) as " +
                "( " +
                "select * from m1 " +
                "UNION " +
                "select * from m2 " +
                ") " +
                "select * from m3").setParameter("patentid", patentId.intValue())
                .setParameter("patentidlist", patentIds)
                .getResultList();

        for (final Object value : patentid) {
            final Object objArray[] = (Object[]) value;
            patentRelationMap.put(Long.valueOf(objArray[0].toString()), Long.valueOf(objArray[1].toString()));
        }

        return patentRelationMap;
    }

    private String getPatentIds(final List<Long> patentIdList) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < patentIdList.size(); i++) {
            stringBuilder.append(patentIdList.get(i));

            if (patentIdList.size() - 1 != i) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public List<Long> getLatestPatentIds(Long count) {

        final List<BigInteger> patentIds = entityManager.createNativeQuery("select t.id from t_patent_info t " +
                "where t.c_filling_date is not null " +
                "order by t.c_filling_date desc " +
                "limit :patentcount").setParameter("patentcount", count).getResultList();
        return patentIds.stream().map(BigInteger::longValue).collect(Collectors.toList());
    }

    @Override
    public BigInteger getMutualWordCount(final Long id, final Long firstClusterNumber, final Long secondClusterNumber) {

        String query = "select count(*) from (" +
                "(SELECT distinct (s.c_word) from t_split_word s " +
                "inner join t_patent_info  p on p.id = s.c_patent_info_id " +
                "inner join t_lsp_info lp on lp.id = p.c_lsp_id " +
                "inner join t_lsr_info r on r.id = lp.c_lsr_id " +
                "where r.id = 574 and p.id in (select r.patentid from result_export r where r.id= :id and cluster= :firstClusterNumber) " +
                "and s.c_word not in (select a.c_word from t_add_word_info a) " +
                "group by s.c_word) " +
                "INTERSECT " +
                "(SELECT distinct(s.c_word) from t_split_word s " +
                "inner join t_patent_info  p on p.id = s.c_patent_info_id " +
                "inner join t_lsp_info lp on lp.id = p.c_lsp_id " +
                "inner join t_lsr_info r on r.id = lp.c_lsr_id " +
                "where r.id = 574 and p.id in (select r.patentid from result_export r where r.id= :id and cluster= :secondClusterNumber) " +
                "and s.c_word not in (select a.c_word from t_add_word_info a) " +
                "group by s.c_word)) as klm";

        return (BigInteger) entityManager.createNativeQuery(query).setParameter("id", id).setParameter("firstClusterNumber", firstClusterNumber)
                .setParameter("secondClusterNumber", secondClusterNumber).getSingleResult();
    }

    @Override
    public BigInteger getPatentCount(final Long id, final Long clusterNumber) {
        final String query = "select count(r.patentid) from result_export r " +
                "where r.id= :id and r.cluster= :cluster";

        return (BigInteger) entityManager.createNativeQuery(query).setParameter("id", id).setParameter("cluster", clusterNumber)
                .getSingleResult();
    }
}