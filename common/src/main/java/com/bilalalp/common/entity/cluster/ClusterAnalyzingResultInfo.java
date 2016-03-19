package com.bilalalp.common.entity.cluster;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = ClusterAnalyzingResultInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ClusterAnalyzingResultInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_CLUSTERING_ANALYZING_RESULT";
    public static final String JOIN_COLUMN = "C_CLUSTERING_ANALYZING_RESULT_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD_COUNT_IN_CLUSTER")
    private Long wordCountInCluster;

    @Column(name = WordSummaryInfo.JOIN_COLUMN)
    private Long wordId;

    @Column(name = ClusterAnalyzingProcessInfo.JOIN_COLUMN)
    private Long clusterAnalyzingProcessInfoId;

    @Column(name = "C_CLUSTER_NUMBER")
    private Long clusterNumber;

    @Column(name = ClusteringRequestInfo.JOIN_COLUMN)
    private Long clusteringRequestId;

    @Column(name = "C_PATENT_COUNT_WITH_OUT_CLUSTER")
    private Long patentCountWithOutCluster;

    @Column(name = "C_TOTAL_PATENT_COUNT")
    private Long totalPatentCountInOtherCluster;

    @Column(name = "C_RESULT")
    private Double result;
}