package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = WordElimination.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class WordElimination extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_WORD_ELIMINATION_ID";
    public static final String TABLE_NAME = "T_WORD_ELIMINATION";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD")
    private String word;

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

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;

    @Column(name = "C_THRESHOLD_VALUE")
    private Long thresholdValue;
}
