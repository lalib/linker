package com.bilalalp.common.repository;


import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;

import java.util.List;

public interface SplitWordInfoCustomRepository {

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    List<PatentWordCountDto> getWordCount(Long patentId);

    List<Long> getExceptedWordIdList(TfIdfRequestInfo tfIdfRequestInfo, List<Long> wordIds);

    Long getPatentWordCountWithoutZeroCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);
}