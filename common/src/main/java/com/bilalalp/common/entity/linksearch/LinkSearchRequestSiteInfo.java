package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.site.SiteInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = LinkSearchRequestSiteInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchRequestSiteInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_LSR_SITE_ID";
    public static final String TABLE_NAME = "T_LSR_SITE_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = SiteInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = SiteInfo.JOIN_COLUMN)
    private SiteInfo siteInfo;

    @ManyToOne(targetEntity = LinkSearchRequestInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = LinkSearchRequestInfo.JOIN_COLUMN)
    private LinkSearchRequestInfo linkSearchRequestInfo;
}