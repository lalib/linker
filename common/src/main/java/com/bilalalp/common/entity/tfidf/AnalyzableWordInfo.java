package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = AnalyzableWordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class AnalyzableWordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_ANALYZABLE_WORD_INFO_ID";

    public static final String TABLE_NAME = "T_ANALYZABLE_WORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD_ID")
    private Long wordId;

    @ManyToOne(targetEntity = TfIdfRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = TfIdfRequestInfo.JOIN_COLUMN)
    private TfIdfRequestInfo tfIdfRequestInfo;
}
