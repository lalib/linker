package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingRequestInfo;
import com.bilalalp.common.repository.ClusterAnalyzingRequestInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ClusterAnalyzingRequestInfoServiceImpl extends AbstractService<ClusterAnalyzingRequestInfo> implements ClusterAnalyzingRequestInfoService {

    @Autowired
    private ClusterAnalyzingRequestInfoRepository repository;
}