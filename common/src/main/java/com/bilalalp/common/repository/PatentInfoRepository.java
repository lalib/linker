package com.bilalalp.common.repository;

import com.bilalalp.common.entity.PatentInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentInfoRepository extends CrudRepository<PatentInfo, Long> {
}
