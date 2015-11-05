package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = LinkSearchRequestInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchRequestInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_LSR_ID";
    public static final String TABLE_NAME = "T_LSR_INFO";

    public LinkSearchRequestInfo() {
        linkSearchGeneratedLinkInfoList = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_LSR_STATUS")
    private LinkSearchRequestStatusType linkSearchRequestStatusType = LinkSearchRequestStatusType.WAITING;

    @OneToMany(targetEntity = LinkSearchRequestSiteInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSearchRequestInfo")
    private List<LinkSearchRequestSiteInfo> linkSearchRequestSiteInfoList;

    @OneToMany(targetEntity = LinkSearchRequestKeywordInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSearchRequestInfo")
    private List<LinkSearchRequestKeywordInfo> linkSearchRequestKeywordInfoList;

    @OneToMany(targetEntity = LinkSearchPageInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSearchRequestInfo")
    private List<LinkSearchPageInfo> linkSearchPageInfoList;

    @OneToMany(targetEntity = LinkSearchGeneratedLinkInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSearchRequestInfo")
    private List<LinkSearchGeneratedLinkInfo> linkSearchGeneratedLinkInfoList;
}