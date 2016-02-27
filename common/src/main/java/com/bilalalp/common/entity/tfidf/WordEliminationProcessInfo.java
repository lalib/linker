package com.bilalalp.common.entity.tfidf;


import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = WordEliminationProcessInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class WordEliminationProcessInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_WORD_ELIMINATION_PROCESS_ID";
    public static final String TABLE_NAME = "T_WORD_EP_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;

    @ManyToOne(targetEntity = WordSummaryInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name = WordSummaryInfo.JOIN_COLUMN)
    private WordSummaryInfo wordSummaryInfo;

    @Column(name = "C_THRESHOLD_VALUE")
    private Long thresholdValue;

    @Column(name = "C_PATENT_COUNT")
    private Long patentCount;
}