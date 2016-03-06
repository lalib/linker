package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = WordEliminationRequestInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class WordEliminationRequestInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_WORD_ELIMINATION_REQUEST_ID";
    public static final String TABLE_NAME = "T_WORD_ER_INFO";

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    public LinkSearchRequestInfo linkSearchRequestInfo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_THRESHOLD_VALUE")
    private Long thresholdValue;
}