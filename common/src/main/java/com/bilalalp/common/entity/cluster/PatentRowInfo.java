package com.bilalalp.common.entity.cluster;

import com.bilalalp.common.entity.base.AbstractEntity;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = PatentRowInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class PatentRowInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_PATENT_ROW_NUMBER_INFO";
    public static final String JOIN_COLUMN = "C_T_PATENT_ROW_NUMBER_INFO_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = TfIdfRequestInfo.JOIN_COLUMN)
    private Long tfIdfRequestInfoId;

    @Column(name = PatentInfo.JOIN_COLUMN)
    private Long patentId;

    @Column(name = "C_ROW_NUMBER")
    private Integer rowNumber;
}