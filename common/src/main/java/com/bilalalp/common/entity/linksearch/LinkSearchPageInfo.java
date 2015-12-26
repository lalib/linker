package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.site.SiteInfoType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = LinkSearchPageInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchPageInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_LSP_ID";
    public static final String TABLE_NAME = "T_LSP_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_START_PAGE")
    private Integer startPage;

    @Column(name = "C_END_PAGE")
    private Integer endPage;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_SITE_INFO_TYPE")
    private SiteInfoType siteInfoType;

    @Lob
    @Column(name = "C_GENERATED_LINK")
    private String generatedLink;

    @OneToMany(targetEntity = PatentInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSearchPageInfo")
    private List<PatentInfo> patentInfoList;
}