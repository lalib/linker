package com.bilalalp.common.repository;


import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.dto.WordProcessorDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;

import java.util.List;

public interface SplitWordInfoCustomRepository {

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, String word);

    Long getPatentWordCountWithoutZeroCount(LinkSearchRequestInfo linkSearchRequestInfo, String word);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, String word);
}