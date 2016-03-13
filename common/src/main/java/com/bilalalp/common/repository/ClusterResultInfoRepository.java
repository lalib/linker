package com.bilalalp.common.repository;


import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterResultInfoRepository extends CrudRepository<ClusteringResultInfo, Long> {

}
