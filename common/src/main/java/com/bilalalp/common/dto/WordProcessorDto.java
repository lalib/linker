package com.bilalalp.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordProcessorDto {

    private Long dfValue;

    private Long patentCount;

    public WordProcessorDto(final Long dfValue, final Long patentCount) {
        this.dfValue = dfValue;
        this.patentCount = patentCount;
    }
}