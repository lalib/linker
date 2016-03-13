package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.repository.ClusteringRequestInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ClusteringRequestInfoServiceImpl extends AbstractService<ClusteringRequestInfo> implements ClusteringRequestInfoService {

    @Autowired
    private ClusteringRequestInfoRepository repository;
}
