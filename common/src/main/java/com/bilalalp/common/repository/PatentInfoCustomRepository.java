package com.bilalalp.common.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface PatentInfoCustomRepository {
    List<Long> getPatentIds(Long limit);

    Map<Long, Long> getPatentRelationMap(Long patentId, List<Long> patentIds);

    List<Long> getLatestPatentIds(Long count);

    BigInteger getMutualWordCount(final Long id, final Long firstClusterNumber, final Long secondClusterNumber);

    BigInteger getPatentCount(Long id, Long clusterNumber);
}
