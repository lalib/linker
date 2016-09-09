package com.bilalalp.common.repository;


import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface SplitWordInfoCustomRepository {

    List<PatentWordCountDto> getPatentWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    List<PatentWordCountDto> getWordCount(Long patentId);

    List<PatentWordCountDto> getWordCount(Long patentId, List<Long> wordIds,Long tfIdfRequestId);

    List<BigInteger> getWords(Long lsrId);

    List<Long> getExceptedWordIdList(TfIdfRequestInfo tfIdfRequestInfo, List<Long> wordIds);

    Long getPatentWordCountWithoutZeroCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    Long getSplitWordCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId);

    List<String> getWordIdsByClusterIdAndLimit(Long clusteringRequestId, Long clusterNumber, Long wordLimit);

    Long getWordCountInACluster(Long clusterNumber, Long clusterRequestId, Long wordId);

    Long getTotalPatentCountInOtherClusters(Long clusterNumber, Long clusterRequestId, Long wordId);

    Map<BigInteger, BigInteger> getPatentWordCounts(Long tvId);

    BigInteger getMutualWordCount(String firstWord, String secondWord);

    Map<String, BigInteger> getExcludedMutualWordCountMap(String word, Long limitCount);

    Map<BigInteger, BigInteger> getExcludedMutualPatentCountMap(Long patentId);

    Map<BigInteger, BigInteger> getPatentValues(Long patentId, Long patentCount);

    List<String> getTopWords(Long patentId);
}