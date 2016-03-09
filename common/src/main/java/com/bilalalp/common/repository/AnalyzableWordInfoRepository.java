package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.AnalyzableWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyzableWordInfoRepository extends CrudRepository<AnalyzableWordInfo, Long> {

    @Query("SELECT a.id FROM AnalyzableWordInfo a WHERE a.tfIdfRequestInfo = :tfIdfRequestInfo AND a.wordId NOT IN :idList")
    List<Long> getWordIds(@Param("tfIdfRequestInfo") TfIdfRequestInfo tfIdfRequestInfo, @Param("idList") List<Long> idList);

    @Query("SELECT a.id FROM AnalyzableWordInfo a WHERE a.tfIdfRequestInfo =:tfIdfRequestInfo")
    List<Long> getWordIds(@Param("tfIdfRequestInfo") TfIdfRequestInfo tfIdfRequestInfo);
}