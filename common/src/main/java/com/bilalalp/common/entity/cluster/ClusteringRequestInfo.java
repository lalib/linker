package com.bilalalp.common.entity.cluster;


import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = ClusteringRequestInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ClusteringRequestInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_CLUSTERING_REQUEST_INFO";
    public static final String JOIN_COLUMN = "C_CLUSTERING_REQUEST_INFO_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_CLUSTER_NUMBER")
    private Long clusterNumber;

    @Column(name = TfIdfRequestInfo.JOIN_COLUMN)
    private Long tfIdfRequestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_CLUSTERING_TYPE")
    private ClusteringType clusteringType;
}
