package com.bilalalp.common.entity.patent;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = WordSummaryInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class WordSummaryInfo extends AbstractEntity{

    public static final String JOIN_COLUMN = "C_WORD_SUMMARY_ID";
    public static final String TABLE_NAME = "T_WORD_SUMMARY_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD")
    private String word;

    @Column(name = "C_COUNT")
    private Long count;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;
}