package com.bilalalp.common.service;

import com.bilalalp.common.dto.EntityDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PatentInfoService extends BaseService<PatentInfo> {

    List<Long> getPatentIdsByDate(Date date);

    List<PatentInfo> getPatentListBylinkSearchPageInfo(LinkSearchPageInfo linkSearchPageInfo);

    List<Long> getPatentIds(Long requestId);

    void resetParseInformation(Long requestId);

    Long getPatentInfoCountByLinkSearchPageInfo(final LinkSearchRequestInfo linkSearchRequestInfo);

    List<EntityDto> getPatentInfos(Long lsrId, Long word);

    List<Long> getPatentIds(Long requestId, String word);

    List<Long> getPatentIdsWithLimit(Long limit);

    Map<Long, Long> getPatentRelationMap(Long patentId, List<Long> patentIds);

    List<Long> getLatestPatentIds(Long count);

    BigInteger getMutualWordCount(final Long id, final Long firstClusterNumber, final Long secondClusterNumber);

    BigInteger getPatentCount(Long id, Long clusterNumber);
}