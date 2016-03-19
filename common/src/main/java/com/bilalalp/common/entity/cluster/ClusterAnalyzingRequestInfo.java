package com.bilalalp.common.entity.cluster;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = ClusterAnalyzingRequestInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ClusterAnalyzingRequestInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_CLUSTER_ANALYZE_REQUEST_INFO";
    public static final String JOIN_COLUMN = "C_CLUSTER_ANALYZE_REQUEST_INFO_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = ClusteringRequestInfo.JOIN_COLUMN)
    private Long clusteringRequestInfoId;

    @Column(name = "C_WORD_LIMIT")
    private Long wordLimit;
}