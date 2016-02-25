package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TfIdfInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TfIdfInfoRepository extends CrudRepository<TfIdfInfo, Long> {
}
