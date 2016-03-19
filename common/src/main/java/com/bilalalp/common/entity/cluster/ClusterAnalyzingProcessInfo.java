package com.bilalalp.common.entity.cluster;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = ClusterAnalyzingProcessInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ClusterAnalyzingProcessInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_CLUSTER_ANALYZE_PROCESS_INFO";
    public static final String JOIN_COLUMN = "C_CLUSTER_ANALYZE_PROCESS_INFO_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_CLUSTER_NUMBER")
    private Long clusterNumber;

    @Column(name = WordSummaryInfo.JOIN_COLUMN)
    private Long wordId;

    @Column(name = ClusterAnalyzingRequestInfo.JOIN_COLUMN)
    private Long clusterAnalyzingRequestInfoId;
}