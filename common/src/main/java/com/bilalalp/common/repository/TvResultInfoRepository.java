package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TvResultInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvResultInfoRepository extends JpaRepository<TvResultInfo, Long> {

    TvResultInfo findByWordId(Long wordId);
}