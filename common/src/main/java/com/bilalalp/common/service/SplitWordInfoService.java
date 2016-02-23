package com.bilalalp.common.service;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SplitWordInfoService extends BaseService<SplitWordInfo> {

    void deleteByRequestId(Long requestId);

    Long getCountByPatentInfoIdAndWord(Long patentInfoId, String word);

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, String word);

    Long getWordCountByLinkSearchRequestInfoAndWord(LinkSearchRequestInfo linkSearchRequestInfo, String word);

    Long getPatentWordCountWithoutZeroCount(final LinkSearchRequestInfo linkSearchRequestInfo, final String word);
}