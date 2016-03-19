package com.bilalalp.common.service;

import com.bilalalp.common.entity.cluster.ClusterAnalyzingResultInfo;
import com.bilalalp.common.repository.ClusterAnalyzingResultInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ClusterAnalyzingResultInfoServiceImpl extends AbstractService<ClusterAnalyzingResultInfo> implements ClusterAnalyzingResultInfoService {

    @Autowired
    private ClusterAnalyzingResultInfoRepository repository;
}
