package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvProcessInfoRepository extends JpaRepository<TvProcessInfo, Long> {

    @Query("select p from TvProcessInfo p order by p.tfIdfValue desc")
    List<TvProcessInfo> findByLimit(Pageable pageable);
}