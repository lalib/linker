package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TfIdfInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class TfIdfInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_TF_IDF_INFO_ID";
    public static final String TABLE_NAME = "T_TF_IDF_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = WordSummaryInfo.JOIN_COLUMN)
    private Long wordInfoId;

    @Column
    private Long patentInfoId;

    @Column(name = "C_SCORE")
    private Double score;

    @Column(name = "C_COUNT")
    private Long count;

    @Column(name = "C_PATENT_COUNT")
    private Long patentCount;

    @Column(name = "C_LOG_VALUE")
    private Double logValue;

    @Column(name = "C_DF_VALUE")
    private Long dfValue;

    @Column(name = "C_TF_VALUE")
    private Long tfValue;

    @Column(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private Long linkSearchRequestInfoId;

    @Column(name = "C_THRESHOLD_VALUE")
    private Long thresholdValue;

    @Column
    private Long tfIdfRequestInfoId;
}