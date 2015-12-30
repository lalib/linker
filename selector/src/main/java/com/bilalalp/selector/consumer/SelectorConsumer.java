package com.bilalalp.selector.consumer;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.patent.KeywordSelectionRequest;
import com.bilalalp.common.service.KeywordSelectionRequestService;
import com.bilalalp.selector.service.SelectorService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelectorConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private SelectorService selectorService;

    @Autowired
    private KeywordSelectionRequestService keywordSelectionRequestService;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        final KeywordSelectionRequest keywordSelectionRequest = keywordSelectionRequestService.find(queueMessageDto.getId());
        selectorService.selectKeyword(keywordSelectionRequest.getFirstRequestId(), keywordSelectionRequest.getSecondRequestId());
    }
}