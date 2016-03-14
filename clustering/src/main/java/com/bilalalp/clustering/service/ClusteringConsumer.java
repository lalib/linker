package com.bilalalp.clustering.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringType;
import com.bilalalp.common.service.ClusteringRequestInfoService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
public class ClusteringConsumer implements MessageListener , Serializable{

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ClusteringRequestInfoService clusteringRequestInfoService;

    @Autowired
    private KmeansClusteringService kmeansClusterService;

    @Autowired
    private PatentFileRowMapperService patentFileRowMapperService;

    @Transactional
    @Override
    public void onMessage(final Message message) {

        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        final ClusteringRequestInfo clusteringRequestInfo = clusteringRequestInfoService.find(queueMessageDto.getId());

        if (ClusteringType.KMEANS.equals(clusteringRequestInfo.getClusteringType())) {
            patentFileRowMapperService.insertPatentRowsToDatabase(clusteringRequestInfo);
            kmeansClusterService.cluster(clusteringRequestInfo);
        }
    }
}