package com.bilalalp.common.service;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.service.base.BaseService;

import java.util.List;

public interface SplitWordInfoService extends BaseService<SplitWordInfo> {

    void deleteByRequestId(Long requestId);

    Long getCountByPatentInfoIdAndWord(Long patentInfoId, Long word);

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getWordCountByLinkSearchRequestInfoAndWord(LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getPatentWordCountWithoutZeroCount(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long word);
}