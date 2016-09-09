package com.bilalalp.common.service;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.repository.SplitWordInfoCustomRepository;
import com.bilalalp.common.repository.SplitWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Getter
@Service
public class SplitWordInfoServiceImpl extends AbstractService<SplitWordInfo> implements SplitWordInfoService {

    @Autowired
    private SplitWordInfoRepository repository;

    @Autowired
    private SplitWordInfoCustomRepository splitWordInfoCustomRepository;

    @Override
    public List<PatentWordCountDto> getWordCount(final Long patentId, final List<Long> wordIds, final Long tfIdfRequestId) {
        return splitWordInfoCustomRepository.getWordCount(patentId, wordIds, tfIdfRequestId);
    }

    @Transactional
    @Override
    public void deleteByRequestId(final Long requestId) {
        repository.deleteByRequestId(requestId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getCountByPatentInfoIdAndWord(final Long patentInfoId, final Long wordInfoId) {
        return repository.getCountByPatentInfoIdAndWord(patentInfoId, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<PatentWordCountDto> getPatentWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return splitWordInfoCustomRepository.getPatentWordCount(linkSearchRequestInfo, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getWordCountByLinkSearchRequestInfoAndWord(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return repository.getWordCountByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getPatentWordCountWithoutZeroCount(LinkSearchRequestInfo linkSearchRequestInfo, Long wordInfoId) {
        return splitWordInfoCustomRepository.getPatentWordCountWithoutZeroCount(linkSearchRequestInfo, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getSplitWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return splitWordInfoCustomRepository.getSplitWordCount(linkSearchRequestInfo, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<PatentWordCountDto> getWordCount(Long patentId) {
        return splitWordInfoCustomRepository.getWordCount(patentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Long> getExceptedWordIdList(TfIdfRequestInfo tfIdfRequestInfo, List<Long> wordIds) {
        return splitWordInfoCustomRepository.getExceptedWordIdList(tfIdfRequestInfo, wordIds);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getWordsByClusterIdAndLimit(final Long clusteringRequestId, final Long clusterNumber, final Long wordLimit) {
        return splitWordInfoCustomRepository.getWordIdsByClusterIdAndLimit(clusteringRequestId, clusterNumber, wordLimit);
    }

    @Override
    public Long getWordCountInACluster(Long clusterNumber, Long clusterRequestId, Long wordId) {
        return splitWordInfoCustomRepository.getWordCountInACluster(clusterNumber, clusterRequestId, wordId);
    }

    @Override
    public Long getTotalPatentCountInOtherClusters(final Long clusterNumber, final Long clusterRequestId, final Long wordId) {
        return splitWordInfoCustomRepository.getTotalPatentCountInOtherClusters(clusterNumber, clusterRequestId, wordId);
    }

    @Override
    public List<BigInteger> getWords(final Long lsrId) {
        return splitWordInfoCustomRepository.getWords(lsrId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<BigInteger, BigInteger> getPatentWordCounts(Long tvId) {
        return splitWordInfoCustomRepository.getPatentWordCounts(tvId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public BigInteger getMutualWordCount(String firstWord, String secondWord) {
        return splitWordInfoCustomRepository.getMutualWordCount(firstWord, secondWord);
    }

    @Override
    public Map<String, BigInteger> getExcludedMutualWordCountMap(String word, Long limitCount) {
        return splitWordInfoCustomRepository.getExcludedMutualWordCountMap(word, limitCount);
    }

    @Override
    public Map<BigInteger, BigInteger> getExcludedMutualPatentCountMap(Long patentId) {
        return splitWordInfoCustomRepository.getExcludedMutualPatentCountMap(patentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<BigInteger, BigInteger> getPatentValues(Long patentId,long patentCount) {
        return splitWordInfoCustomRepository.getPatentValues(patentId,patentCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getTopWords(final Long patentId) {
        return splitWordInfoCustomRepository.getTopWords(patentId);
    }
}