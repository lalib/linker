package com.bilalalp.common.repository;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingRequestInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterAnalyzingRequestInfoRepository extends CrudRepository<ClusterAnalyzingRequestInfo, Long> {
}
