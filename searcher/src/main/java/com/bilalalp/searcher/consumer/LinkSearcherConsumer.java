package com.bilalalp.searcher.consumer;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.LinkSearchPageInfoService;
import com.bilalalp.searcher.service.SearcherService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

@Service
public class LinkSearcherConsumer implements MessageListener {

    private static final Map<SiteInfoType, SearcherService> SEARCHER_SERVICE_MAP = new EnumMap<>(SiteInfoType.class);
    @Autowired
    private MessageConverter messageConverter;
    @Autowired
    private LinkSearchPageInfoService linkSearchPageInfoService;
    @Autowired
    private ApplicationContext applicationContext;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        final LinkSearchPageInfo linkSearchPageInfo = linkSearchPageInfoService.find(queueMessageDto.getId());
        SEARCHER_SERVICE_MAP.get(linkSearchPageInfo.getSiteInfoType()).search(linkSearchPageInfo);
    }

    @PostConstruct
    public void init() {
        final Collection<SearcherService> values = applicationContext.getBeansOfType(SearcherService.class).values();
        for (final SearcherService searcherService : values) {
            SEARCHER_SERVICE_MAP.put(searcherService.getSiteInfoType(), searcherService);
        }
    }
}