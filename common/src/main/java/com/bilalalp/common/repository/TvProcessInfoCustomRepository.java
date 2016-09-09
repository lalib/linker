package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TvProcessInfoCustomRepository {

    List<TvProcessInfo> findByLimitWithoutAdditionalWords(Long count);

    List<TvProcessInfo> findByLimit(Long limit,  List<Long> patentIds);
}
