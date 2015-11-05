package com.bilalalp.searcher.consumer;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.service.LinkSearchPageInfoService;
import com.bilalalp.searcher.service.PatentScopeSearcherService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkSearcherConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private LinkSearchPageInfoService linkSearchPageInfoService;

    @Autowired
    private PatentScopeSearcherService patentScopeSearcherService;

    @Transactional
    @Override
    public void onMessage(Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId().toString());

        final LinkSearchPageInfo linkSearchPageInfo = linkSearchPageInfoService.find(queueMessageDto.getId());
        patentScopeSearcherService.search(linkSearchPageInfo);
    }
}