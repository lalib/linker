package com.bilalalp.common.repository;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<PatentWordCountDto> getWordCount(final Long patentId, final List<Long> wordIds, final Long tfIdfRequestId) {
        return entityManager
                .createQuery("SELECT new com.bilalalp.common.dto.PatentWordCountDto(w.id,w.version, COUNT(s.word)) " +
                        "FROM SplitWordInfo s " +
                        "INNER JOIN WordSummaryInfo w ON w.word = s.word " +
                        "INNER JOIN AnalyzableWordInfo a ON a.wordId = w.id " +
                        "WHERE s.patentInfo.id = :patentId AND a.wordId IN (:wordIds) AND a.tfIdfRequestInfo.id = :tfIdfRequestId " +
                        "GROUP BY w.id,w.version " +
                        "ORDER BY COUNT(s.word) DESC")
                .setParameter("patentId", patentId)
                .setParameter("wordIds", wordIds)
                .setParameter("tfIdfRequestId", tfIdfRequestId)
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

    @Override
    public Map<BigInteger, BigInteger> getPatentWordCounts(final Long tvId) {

        List resultList = entityManager.createNativeQuery("select * from " +
                "((select p.id as pid,count(s.c_word) from t_patent_info p " +
                "inner join t_split_word s on s.c_patent_info_id\t= p.id " +
                "where s.c_word in (select k.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info k on k.id = pi.wordid " +
                "where pi.id= :tvId) " +
                "group by p.id) " +
                "union " +
                "(select o.id  as pid,0 from t_patent_info o " +
                "where o.id not in " +
                "(select p.id from t_patent_info p  " +
                "inner join t_split_word s on s.c_patent_info_id = p.id " +
                "where s.c_word in (select k.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info k on k.id = pi.wordid " +
                "where pi.id= :tvId) " +
                "group by p.id))) as klm " +
                "order by klm.pid desc").setParameter("tvId", tvId).getResultList();

        final Map<BigInteger, BigInteger> resultMap = new HashMap<>();

        for (final Object obj : resultList) {

            final Object resultArray[] = (Object[]) obj;
            resultMap.put(BigInteger.valueOf(Long.valueOf(resultArray[0].toString())), BigInteger.valueOf(Long.valueOf(resultArray[1].toString())));
        }

        return resultMap;
    }

    @Override
    public BigInteger getMutualWordCount(final String firstWord, final String secondWord) {

        return (BigInteger) entityManager.createNativeQuery("select count(p.id) from t_patent_info p " +
                "where exists (select * from t_split_word s where s.c_patent_info_id = p.id AND s.c_word = :firstWord) " +
                "and exists (select * from t_split_word s where s.c_patent_info_id = p.id AND s.c_word = :secondWord)")
                .setParameter("firstWord", firstWord)
                .setParameter("secondWord", secondWord)
                .getSingleResult();
    }

    @Override
    public Map<String, BigInteger> getExcludedMutualWordCountMap(String word, Long limitCount) {

        final List resultList = entityManager.createNativeQuery("select * from " +
                "((select s.c_word as kelime,count(DISTINCT(p.id)) from t_patent_info p " +
                "inner join t_split_word s on s.c_patent_info_id = p.id " +
                "where s.c_word in (select t.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info t on t.id = pi.wordid " +
                "order by pi.tvresult desc limit :limitCount) " +
                "and exists (select * from t_split_word s where s.c_patent_info_id = p.id AND s.c_word = :word) " +
                "group by s.c_word) " +
                "union " +
                "(select f.kelime as kelime,0 from " +
                "((select t.c_word as kelime from t_tv_process_inf pi " +
                "inner join t_word_summary_info t on t.id = pi.wordid " +
                "order by pi.tvresult desc limit :limitCount) " +
                "except " +
                "(select s.c_word as kelime from t_patent_info p " +
                "inner join t_split_word s on s.c_patent_info_id = p.id " +
                "where s.c_word in (select t.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info t on t.id = pi.wordid " +
                "order by pi.tvresult desc limit :limitCount) " +
                "and exists (select * from t_split_word s where s.c_patent_info_id = p.id AND s.c_word = :word) " +
                "group by s.c_word)) as f)) as jnm " +
                "order by jnm.kelime")
                .setParameter("word", word)
                .setParameter("limitCount", limitCount).getResultList();


        final Map<String, BigInteger> wordCountMap = new HashMap<>();

        for (final Object obj : resultList) {
            final Object row[] = (Object[]) obj;
            wordCountMap.put(row[0].toString(), BigInteger.valueOf(Long.valueOf(row[1].toString())));
        }

        return wordCountMap;
    }

    @Override
    public Map<BigInteger, BigInteger> getExcludedMutualPatentCountMap(final Long patentId) {

        final List pid = entityManager.createNativeQuery("select p1.id as pid ,count(distinct(t1.c_word)) from t_patent_info p1 " +
                "inner join t_split_word t1 on t1.c_patent_info_id = p1.id " +
                "where p1.id != :pid and t1.c_word in (select distinct s.c_word from t_split_word s " +
                "inner join t_patent_info p on p.id = s.c_patent_info_id " +
                "where p.id= :pid) " +
                "group by p1.id ")
                .setParameter("pid", patentId).getResultList();

        final Map<BigInteger, BigInteger> bigIntegerMap = new HashMap<>();

        for (Object obj : pid) {
            final Object[] val = (Object[]) obj;
            bigIntegerMap.put(BigInteger.valueOf(Long.valueOf(val[0].toString())), BigInteger.valueOf(Long.valueOf(val[1].toString())));
        }

        return bigIntegerMap;
    }

    @Override
    public Map<BigInteger, BigInteger> getPatentValues(final Long patentId, final Long patentCount) {

        final List patentlist = entityManager.createNativeQuery("with m1(patentid) as " +
                "(select t.id from t_patent_info t " +
                "where t.c_filling_date is not null " +
                "order by t.c_filling_date desc " +
                "limit :patentcount), " +
                "m2(patentid,mcount)  as (select p.patentid,count(distinct(s.c_word)) from t_split_word s " +
                "inner join m1 p on p.patentid = s.c_patent_info_id " +
                "where s.c_word in (select DISTINCT(k.c_word) from t_split_word k where k.c_patent_info_id = :patentId) " +
                "and s.c_word not in ( " +
                "select s.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info s on s.id = pi.wordid " +
                "where " +
                "s.c_word  in (SELECT mytv.word " +
                "FROM dblink('host=localhost user=postgres password=postgres dbname=linker4','select s.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info s on s.id = pi.wordid') " +
                "   AS mytv(word varchar)) " +
                "and  " +
                "s.c_word  in  " +
                "(SELECT mytv.word " +
                "FROM dblink('host=localhost user=postgres password=postgres dbname=linker','select s.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info s on s.id = pi.wordid') " +
                "    AS mytv(word varchar)) " +
                "and s.c_word  in " +
                "(SELECT mytv.word " +
                "FROM dblink('host=localhost user=postgres password=postgres dbname=linker2','select s.c_word from t_tv_process_inf pi " +
                "inner join t_word_summary_info s on s.id = pi.wordid') " +
                "    AS mytv(word varchar))) " +
                "group by p.patentid),  " +
                "m3(patentid,mcount) as   " +
                "((select n.patentid,0 from m1 n  " +
                "where n.patentid not in (select m.patentid from m2 m)) " +
                "union  " +
                "(select * from m2)) " +
                "select * from m3 " +
                "order by m3.patentid desc").setParameter("patentId", patentId)
                .setParameter("patentcount", patentCount).getResultList();

        final Map<BigInteger, BigInteger> patentMap = new HashMap<>();

        for (Object obj : patentlist) {
            final Object[] mArray = (Object[]) obj;
            patentMap.put(BigInteger.valueOf(Long.valueOf(mArray[0].toString())), BigInteger.valueOf(Long.valueOf(mArray[1].toString())));
        }

        return patentMap;
    }

    @Override
    public List<String> getTopWords(final Long patentId) {
        return entityManager.createNativeQuery("select s.c_word from t_split_word s " +
                "where s.c_patent_info_id = :patentId " +
                "group by s.c_word " +
                "order by count(s.c_word) desc " +
                "limit 3").setParameter("patentId", patentId)
                .getResultList();
    }
}