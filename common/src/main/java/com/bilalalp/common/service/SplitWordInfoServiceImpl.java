package com.bilalalp.common.service;

import com.bilalalp.common.dto.PatentWordCountDto;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.SplitWordInfo;
import com.bilalalp.common.repository.SplitWordInfoCustomRepository;
import com.bilalalp.common.repository.SplitWordInfoRepository;
import com.bilalalp.common.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SplitWordInfoServiceImpl extends AbstractService<SplitWordInfo> implements SplitWordInfoService {

    @Autowired
    private SplitWordInfoRepository splitWordInfoRepository;

    @Autowired
    private SplitWordInfoCustomRepository splitWordInfoCustomRepository;

    @Override
    protected CrudRepository<SplitWordInfo, Long> getRepository() {
        return splitWordInfoRepository;
    }

    @Transactional
    @Override
    public void deleteByRequestId(final Long requestId) {
        splitWordInfoRepository.deleteByRequestId(requestId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getCountByPatentInfoIdAndWord(final Long patentInfoId, final Long wordInfoId) {
        return splitWordInfoRepository.getCountByPatentInfoIdAndWord(patentInfoId, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<PatentWordCountDto> getPatentWordCount(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return splitWordInfoCustomRepository.getPatentWordCount(linkSearchRequestInfo, wordInfoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Long getWordCountByLinkSearchRequestInfoAndWord(final LinkSearchRequestInfo linkSearchRequestInfo, final Long wordInfoId) {
        return splitWordInfoRepository.getWordCountByLinkSearchRequestInfoAndWord(linkSearchRequestInfo, wordInfoId);
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
}