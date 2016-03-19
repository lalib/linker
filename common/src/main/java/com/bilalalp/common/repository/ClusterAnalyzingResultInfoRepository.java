package com.bilalalp.common.repository;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingResultInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterAnalyzingResultInfoRepository extends CrudRepository<ClusterAnalyzingResultInfo, Long> {
}
