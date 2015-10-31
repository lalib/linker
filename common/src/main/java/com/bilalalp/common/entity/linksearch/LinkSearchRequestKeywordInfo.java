package com.bilalalp.common.entity.linksearch;


import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = LinkSearchRequestKeywordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchRequestKeywordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_LSR_KEYWORD_ID";
    public static final String TABLE_NAME = "T_LSR_KEYWORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_KEYWORD")
    private String keyword;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;
}