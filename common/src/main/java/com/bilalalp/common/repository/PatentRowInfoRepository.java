package com.bilalalp.common.repository;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentRowInfoRepository extends CrudRepository<PatentRowInfo, Long> {
}
