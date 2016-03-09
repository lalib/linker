package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TfIdfProcessInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class TfIdfProcessInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_TF_IDF_PROCESS_INFO_ID";

    public static final String TABLE_NAME = "T_TF_IDF_PROCESS_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_THRESHOLD_VALUE")
    private Long thresholdValue;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;

    @Column(name = PatentInfo.JOIN_COLUMN)
    private Long patentInfoId;

    @ManyToOne(targetEntity = TfIdfRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = TfIdfRequestInfo.JOIN_COLUMN)
    private TfIdfRequestInfo tfIdfRequestInfo;
}