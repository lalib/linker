package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.SelectedKeywordInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedKeywordInfoRepository extends CrudRepository<SelectedKeywordInfo, Long> {

}