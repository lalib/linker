package com.bilalalp.common.entity;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = PatentInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class PatentInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_PATENT_INFO_ID";
    public static final String TABLE_NAME = "T_PATENT_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_PATENT_NUMBER")
    private String patentNumber;

    @Lob
    @Column(name = "C_PATENT_TITLE")
    private String patentTitle;

    @Lob
    @Column(name = "C_PATENT_LINK")
    private String patentLink;

    @Lob
    @Column(name = "C_BODY")
    private String body;

    @Lob
    @Column(name = "C_ABSTRACT_CONTENT")
    private String abstractContent;

    @Column(name = "C_PARSED")
    private Boolean parsed = Boolean.FALSE;

    @Lob
    @Column(name = "C_SEARCH_LINK")
    private String searchLink;

    @ManyToOne(targetEntity = LinkSearchPageInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchPageInfo.JOIN_COLUMN)
    private LinkSearchPageInfo linkSearchPageInfo;
}
