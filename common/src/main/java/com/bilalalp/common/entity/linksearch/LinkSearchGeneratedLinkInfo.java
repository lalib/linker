package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.site.SiteInfoType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = LinkSearchGeneratedLinkInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchGeneratedLinkInfo {

    public static final String JOIN_COLUMN = "C_LSGL_ID";
    public static final String TABLE_NAME = "T_LSGL_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_SITE_TYPE")
    private SiteInfoType siteInfoType;

    @Lob
    @Column(name = "C_GENERATED_LINK")
    private String generatedLink;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;
}