package com.bilalalp.common.repository;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingProcessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterAnalyzingProcessInfoRepository extends JpaRepository<ClusterAnalyzingProcessInfo, Long> {
}
