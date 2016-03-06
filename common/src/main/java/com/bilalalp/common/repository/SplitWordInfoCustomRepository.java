package com.bilalalp.common.repository;


import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;

import java.util.List;

public interface SplitWordInfoCustomRepository {

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    Long getPatentWordCountWithoutZeroCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);
}