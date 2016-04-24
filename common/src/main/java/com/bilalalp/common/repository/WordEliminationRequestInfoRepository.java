package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.WordEliminationRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordEliminationRequestInfoRepository extends JpaRepository<WordEliminationRequestInfo, Long> {
}
