package com.bilalalp.common.entity.patent;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = PatentClassInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class PatentClassInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_PATENT_CLASS_INFO_ID";
    public static final String TABLE_NAME = "T_PATENT_CLASS_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_CLASS_INFO")
    private String classInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_PATENT_CLASS_INFO_TYPE")
    private PatentClassInfoType patentClassInfoType;

    @ManyToOne(targetEntity = PatentInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = PatentInfo.JOIN_COLUMN)
    private PatentInfo patentInfo;
}