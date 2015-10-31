package com.bilalalp.common.entity.linksearch;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = LinkSearchRequestInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class LinkSearchRequestInfo extends AbstractEntity{

    public static final String JOIN_COLUMN = "C_LSR_ID";
    public static final String TABLE_NAME = "T_LSR_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}