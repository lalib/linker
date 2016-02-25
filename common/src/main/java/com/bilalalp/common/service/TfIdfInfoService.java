package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfInfo;
import com.bilalalp.common.entity.tfidf.WordElimination;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TfIdfInfoService extends BaseService<TfIdfInfo> {

    void process(Long lsrId, Long thresholdValue);

    void saveWithNewTransaction(WordElimination wordElimination, LinkSearchRequestInfo linkSearchRequestInfo, PatentWordCountDto patentWordCountDto, Long thresholdValue);

    void saveWithNewTransaction(WordElimination wordElimination, LinkSearchRequestInfo linkSearchRequestInfo, List<EntityDto> patentInfos, Long thresholdValue);
}