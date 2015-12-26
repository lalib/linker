package com.bilalalp.common.repository;

import com.bilalalp.common.entity.SplitWordInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitWordInfoRepository extends CrudRepository<SplitWordInfo, Long> {

}