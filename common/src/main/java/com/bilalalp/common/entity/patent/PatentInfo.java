package com.bilalalp.common.entity.patent;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = PatentInfo.TABLE_NAME,indexes = {@Index(name = "INX_ID",columnList = "id")})
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

    @Lob
    @Column(name = "C_CLAIM_CONTENT")
    private String claimContent;

    @Lob
    @Column(name = "C_DESCRIPTION_CONTENT")
    private String descriptionContent;

    @Lob
    @Column(name = "C_INVENTORS")
    private String inventors;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "C_APPLICATION_NUMBER")
    private String applicationNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_FILLING_DATE")
    private Date fillingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_PUBLICATION_DATE")
    private Date publicationDate;

    @Lob
    @Column(name = "C_ASSIGNEE")
    private String assignee;

    @Column(name = "C_PARSED")
    private Boolean parsed = Boolean.FALSE;

    @Lob
    @Column(name = "C_SEARCH_LINK")
    private String searchLink;

    @ManyToOne(targetEntity = LinkSearchPageInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchPageInfo.JOIN_COLUMN)
    private LinkSearchPageInfo linkSearchPageInfo;

    @OneToMany(targetEntity = SplitWordInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patentInfo")
    private List<SplitWordInfo> splitWordInfoList;

    @OneToMany(targetEntity = PatentClassInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "patentInfo")
    private List<PatentClassInfo> patentClassInfoList;

}
