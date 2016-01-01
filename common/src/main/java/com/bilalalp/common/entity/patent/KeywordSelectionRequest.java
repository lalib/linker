package com.bilalalp.common.entity.patent;


import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = KeywordSelectionRequest.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class KeywordSelectionRequest extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_KEYWORD_SELECTION_INFO_ID";
    public static final String TABLE_NAME = "T_KEYWORD_SELECTION";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_FIRST_REQUEST_ID")
    private Long firstRequestId;

    @Column(name = "C_SECOND_REQUEST_ID")
    private Long secondRequestId;

    @Column(name = "C_TOP_SELECTED_KEYWORD_COUNT")
    private Long topSelectedKeywordCount;

    @Column(name = "C_RATIO")
    private Double ratio;
}