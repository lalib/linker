package com.bilalalp.common.entity;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = AdditionalWordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class AdditionalWordInfo extends AbstractEntity{


    public static final String TABLE_NAME = "T_ADD_WORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD")
    private String word;
}