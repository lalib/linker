package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordSelectionRequestRepository extends CrudRepository<KeywordSelectionRequest, Long> {

}