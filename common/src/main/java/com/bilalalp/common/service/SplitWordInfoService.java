package com.bilalalp.common.service;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.base.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface SplitWordInfoService extends BaseService<SplitWordInfo> {

    List<PatentWordCountDto> getWordCount(Long patentId, List<Long> wordIds,Long tfIdfRequestId);

    void deleteByRequestId(Long requestId);

    Long getCountByPatentInfoIdAndWord(Long patentInfoId, Long word);

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getWordCountByLinkSearchRequestInfoAndWord(LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getPatentWordCountWithoutZeroCount(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long word);

    List<PatentWordCountDto> getWordCount(Long patentId);

    List<Long> getExceptedWordIdList(TfIdfRequestInfo tfIdfRequestInfo, List<Long> wordIds);

    List<String> getWordsByClusterIdAndLimit(Long clusteringRequestId, Long clusterNumber, Long wordLimit);

    Long getWordCountInACluster(final Long clusterNumber, final Long clusterRequestId, final Long wordId);

    Long getTotalPatentCountInOtherClusters(Long clusterNumber, Long clusterRequestId, Long wordId);

    List<BigInteger> getWords(Long lsrId);

    Map<BigInteger, BigInteger> getPatentWordCounts(final Long tvId);

    BigInteger getMutualWordCount(final String firstWord,final String secondWord);

    Map<String, BigInteger> getExcludedMutualWordCountMap(String word, Long limitCount);

    Map<BigInteger, BigInteger> getExcludedMutualPatentCountMap(final Long patentId);

    Map<BigInteger, BigInteger> getPatentValues(Long patentId, long patentCount);

    List<String> getTopWords(Long patentId);
}