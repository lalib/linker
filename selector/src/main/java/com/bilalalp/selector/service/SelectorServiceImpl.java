package com.bilalalp.selector.service;

import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import com.bilalalp.common.entity.patent.SelectedKeywordInfo;
import com.bilalalp.common.entity.tfidf.WordSummaryInfo;
import com.bilalalp.common.service.LinkSearchRequestInfoService;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.service.SelectedKeywordInfoService;
import com.bilalalp.common.service.WordSummaryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectorServiceImpl implements SelectorService {

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private SelectedKeywordInfoService selectedKeywordInfoService;

    @Transactional
    @Override
    public void selectKeyword(final KeywordSelectionRequest keywordSelectionRequest) {

        final LinkSearchRequestInfo firstLinkSearchRequestInfo = linkSearchRequestInfoService.find(keywordSelectionRequest.getFirstRequestId());
        final LinkSearchRequestInfo secondLinkSearchRequestInfo = linkSearchRequestInfoService.find(keywordSelectionRequest.getSecondRequestId());
        final List<WordSummaryInfo> wordSummaryInfoList = wordSummaryInfoService.findByLinkSearchRequestInfo(firstLinkSearchRequestInfo, new PageRequest(0, keywordSelectionRequest.getTopSelectedKeywordCount().intValue()));

        final Long patentCount = patentInfoService.getPatentInfoCountByLinkSearchPageInfo(firstLinkSearchRequestInfo);
        final List<WordSummaryInfo> selectedWordSummaryInfoList = new ArrayList<>();

        for (final WordSummaryInfo wordSummaryInfo : wordSummaryInfoList) {

            final Double result = wordSummaryInfo.getCount() / (keywordSelectionRequest.getRatio() * patentCount);
            final WordSummaryInfo secondWordSummaryInfo = wordSummaryInfoService.findByLinkSearchRequestInfoAndWord(secondLinkSearchRequestInfo, wordSummaryInfo.getWord());

            if (secondWordSummaryInfo == null) {
                selectedWordSummaryInfoList.add(wordSummaryInfo);
            } else {

                final Double secondResult = secondWordSummaryInfo.getCount().doubleValue() / 1000L;

                if (result > secondResult) {
                    selectedWordSummaryInfoList.add(wordSummaryInfo);
                }
            }
        }

        final List<SelectedKeywordInfo> selectedKeywordInfoList = createSelectedKeywordInfoList(selectedWordSummaryInfoList, keywordSelectionRequest);
        selectedKeywordInfoService.save(selectedKeywordInfoList);
    }

    private List<SelectedKeywordInfo> createSelectedKeywordInfoList(final List<WordSummaryInfo> wordSummaryInfoList, final KeywordSelectionRequest keywordSelectionRequest) {
        return wordSummaryInfoList
                .stream().map(k -> createSelectedKeywordInfo(keywordSelectionRequest, k))
                .collect(Collectors.toList());

    }

    private SelectedKeywordInfo createSelectedKeywordInfo(final KeywordSelectionRequest keywordSelectionRequest, final WordSummaryInfo k) {
        final SelectedKeywordInfo selectedKeywordInfo = new SelectedKeywordInfo();
        selectedKeywordInfo.setKeywordSelectionRequest(keywordSelectionRequest);
        selectedKeywordInfo.setWordSummaryInfo(k);
        return selectedKeywordInfo;
    }
}