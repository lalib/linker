package com.bilalalp.common.repository;

import com.bilalalp.common.entity.StopWordInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopWordInfoRepository extends CrudRepository<StopWordInfo, Long> {

}