package com.bilalalp.common.entity.patent;

import com.bilalalp.common.entity.base.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = SplitWordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class SplitWordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_SPLIT_WORD_INFO_ID";
    public static final String TABLE_NAME = "T_SPLIT_WORD";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_WORD")
    private String word;

    @Enumerated(EnumType.STRING)
    @Column(name = "C_SPLIT_WORD_TYPE")
    private SplitWordType splitWordType;

    @ManyToOne(targetEntity = PatentInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = PatentInfo.JOIN_COLUMN)
    private PatentInfo patentInfo;
}
