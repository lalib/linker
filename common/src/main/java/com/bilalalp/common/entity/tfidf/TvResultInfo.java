package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TvResultInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class TvResultInfo extends AbstractEntity{

    public static final String TABLE_NAME = "T_TV_RESULT_INF";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long wordId;

    private Double tvResult;

    private Double avg;
}