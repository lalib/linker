package com.bilalalp.common.entity.tfidf;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = WordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class WordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_WORD_INFO_ID";
    public static final String TABLE_NAME = "T_WORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD")
    private String word;
}