package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.WordSummaryInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSummaryInfoRepository extends CrudRepository<WordSummaryInfo, Long> {

}