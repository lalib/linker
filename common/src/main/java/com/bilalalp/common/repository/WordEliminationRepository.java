package com.bilalalp.common.repository;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordEliminationRepository extends JpaRepository<WordElimination, Long> {

    @Query("SELECT p FROM WordElimination p WHERE p.linkSearchRequestInfo = :linkSearchRequestInfo ORDER BY p.count DESC")
    List<WordElimination> getEliminatedWordsByLinkSearchRequestInfoAndThresholdValue(@Param("linkSearchRequestInfo") LinkSearchRequestInfo linkSearchRequestInfo, Pageable pageable);
}