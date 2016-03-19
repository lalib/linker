package com.bilalalp.common.repository;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface PatentRowInfoRepository extends CrudRepository<PatentRowInfo, Long>, Serializable {
}
