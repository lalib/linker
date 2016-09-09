package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.AnalyzableWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyzableWordInfoRepository extends JpaRepository<AnalyzableWordInfo, Long> {

    @Query("SELECT DISTINCT a.wordId FROM AnalyzableWordInfo a WHERE a.tfIdfRequestInfo = :tfIdfRequestInfo AND a.wordId IN :idList and a.wordId NOT IN :inList")
    List<Long> getWordIds(@Param("tfIdfRequestInfo") TfIdfRequestInfo tfIdfRequestInfo, @Param("idList") List<Long> idList, @Param("inList") List<Long> inList);

    @Query("SELECT a.wordId FROM AnalyzableWordInfo a WHERE a.tfIdfRequestInfo =:tfIdfRequestInfo")
    List<Long> getWordIds(@Param("tfIdfRequestInfo") TfIdfRequestInfo tfIdfRequestInfo);

    @Query("SELECT a.wordId from AnalyzableWordInfo a WHERE a.tfIdfRequestInfo = :tfIdfRequestInfo AND a.wordId IN :idList")
    List<Long> getWordIds(@Param("tfIdfRequestInfo") TfIdfRequestInfo tfIdfRequestInfo, @Param("idList") List<Long> idList);
}