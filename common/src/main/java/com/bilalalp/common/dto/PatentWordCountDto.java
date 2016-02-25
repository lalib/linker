package com.bilalalp.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatentWordCountDto implements Serializable{

    private Long patentId;

    private Integer patentVersion;

    private Long wordCount;
}
