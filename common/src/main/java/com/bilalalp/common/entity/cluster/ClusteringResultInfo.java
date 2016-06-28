package com.bilalalp.common.entity.cluster;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = ClusteringResultInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ClusteringResultInfo extends AbstractEntity implements Serializable{

    public static final String TABLE_NAME = "T_CLUSTERING_RESULT_INFO";
    public static final String JOIN_COLUMN = "C_CLUSTERING_RESULT_INFO_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = TfIdfRequestInfo.JOIN_COLUMN)
    private Long tfIdfRequestInfoId;

    @Column(name = PatentInfo.JOIN_COLUMN)
    private Long patentId;

    @Column(name = "C_CLUSTER_NUMBER")
    private Long clusteringNumber;

    @Column(name = "C_WSSSE")
    private Double wssse;

    @Column(name = ClusteringRequestInfo.JOIN_COLUMN)
    private Long clusteringRequestId;
}