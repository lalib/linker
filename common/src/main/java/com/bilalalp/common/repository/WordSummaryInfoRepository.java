package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.WordSummaryInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordSummaryInfoRepository extends CrudRepository<WordSummaryInfo, Long> {

    @Query("Select COUNT(p.id) FROM WordSummaryInfo p WHERE p.linkSearchRequestInfo = :linkSearchRequestInfo AND p.count> :thresholdValue")
    Long getCountByLinkSearchRequestInfoAndThresholdValue(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, @Param("thresholdValue") Long thresholdValue);

    @Query("Select p FROM WordSummaryInfo p WHERE p.linkSearchRequestInfo = :linkSearchRequestInfo AND p.count> :thresholdValue ORDER BY p.count DESC")
    List<WordSummaryInfo> findByLinkSearchRequestInfoAndThresholdValue(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, @Param("thresholdValue") Long thresholdValue, Pageable pageable);

    @Query("Select p FROM WordSummaryInfo p WHERE p.linkSearchRequestInfo = :linkSearchRequestInfo ORDER BY p.count DESC")
    List<WordSummaryInfo> findByLinkSearchRequestInfo(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, Pageable pageable);

    @Query("SELECT p FROM WordSummaryInfo p WHERE p.linkSearchRequestInfo = :linkSearchRequestInfo AND p.word = :word")
    WordSummaryInfo findByLinkSearchRequestInfoAndWord(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, @Param("word") String word);
}