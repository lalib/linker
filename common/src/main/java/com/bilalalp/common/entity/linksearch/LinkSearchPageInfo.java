package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
}