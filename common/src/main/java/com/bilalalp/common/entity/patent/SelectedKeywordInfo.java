package com.bilalalp.common.entity.patent;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = SelectedKeywordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class SelectedKeywordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_SELECTED_KEYWORD_ID";
    public static final String TABLE_NAME = "T_SELECTED_KEYWORD";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = KeywordSelectionRequest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = KeywordSelectionRequest.JOIN_COLUMN)
    private KeywordSelectionRequest keywordSelectionRequest;

    @ManyToOne(targetEntity = WordSummaryInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = WordSummaryInfo.JOIN_COLUMN)
    private WordSummaryInfo wordSummaryInfo;
}