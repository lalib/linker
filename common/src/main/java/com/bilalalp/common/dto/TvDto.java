package com.bilalalp.common.dto;

import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class TvDto {

    private List<TvProcessInfo> tvProcessInfoList;
}