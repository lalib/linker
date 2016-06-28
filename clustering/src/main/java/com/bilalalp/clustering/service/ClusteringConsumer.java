package com.bilalalp.clustering.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringType;
import com.bilalalp.common.service.ClusteringRequestInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Slf4j
@Service
public class ClusteringConsumer implements MessageListener, Serializable {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ClusteringRequestInfoService clusteringRequestInfoService;

    @Autowired
    private KmeansClusteringService kmeansClusterService;

    @Autowired
    private PatentFileRowMapperService patentFileRowMapperService;

    @Autowired
    private GaussianMixtureClusteringService gaussianMixtureClusteringService;

    @Autowired
    private LatentDirichletAllocationClusteringService latentDirichletAllocationClusteringService;

    @Autowired
    private PowerIterationClusteringService powerIterationClusteringService;

    @Autowired
    private DBScanClusteringService dbScanClusteringService;

    @Override
    public void onMessage(final Message message) {

        try {
            final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
            final ClusteringRequestInfo clusteringRequestInfo = clusteringRequestInfoService.find(queueMessageDto.getId());

            if (ClusteringType.KMEANS.equals(clusteringRequestInfo.getClusteringType())) {
//            patentFileRowMapperService.insertPatentRowsToDatabase(clusteringRequestInfo);
                kmeansClusterService.cluster(clusteringRequestInfo);
//            dbScanClusteringService.cluster(clusteringRequestInfo);
//                powerIterationClusteringService.cluster(clusteringRequestInfo);
//            latentDirichletAllocationClusteringService.cluster(clusteringRequestInfo);
//                gaussianMixtureClusteringService.cluster(clusteringRequestInfo);
            } else if (ClusteringType.LDA.equals(clusteringRequestInfo.getClusteringType())) {
                latentDirichletAllocationClusteringService.cluster(clusteringRequestInfo);
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            System.out.println(ex.getMessage());
        }
    }
}