package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface TfIdfRequestInfoRepository extends CrudRepository<TfIdfRequestInfo, Long>, Serializable {

}