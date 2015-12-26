package com.bilalalp.common.entity;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = StopWordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class StopWordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_STOPWORD_ID";
    public static final String TABLE_NAME = "T_STOPWORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String stopWord;
}