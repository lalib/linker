package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.SplitWordType;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvProcessInfoRepository extends JpaRepository<TvProcessInfo, Long> {

    @Query("select p from TvProcessInfo p order by p.tfIdfValue desc")
    List<TvProcessInfo> findByLimit(Pageable pageable);

    @Query("SELECT DISTINCT p FROM TvProcessInfo p, WordSummaryInfo w, PatentInfo i, SplitWordInfo s " +
            "WHERE w.id = p.wordId AND i.id in (:patentIds) AND s.patentInfo.id = i.id " +
            "order by p.tfIdfValue desc")
    List<TvProcessInfo> findByLimit(Pageable pageRequest, @Param("patentIds") List<Long> patentIds);

    @Query("SELECT DISTINCT p FROM TvProcessInfo p, WordSummaryInfo w, PatentInfo i, SplitWordInfo s " +
            "WHERE w.id = p.wordId AND s.patentInfo.id = i.id AND s.splitWordType = :splitWordType AND i.id in (:patentIds) AND s.patentInfo.id = i.id " +
            "order by p.tfIdfValue desc")
    List<TvProcessInfo> findByLimit(Pageable pageable, @Param("splitWordType") SplitWordType splitWordType,@Param("patentIds") List<Long> patentIds);
}