package com.bilalalp.dispatcher.amqp;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.dispatcher.engine.SearcherService;
import lombok.Setter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Service
public class DispatcherRequestConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private SearcherService searcherService;

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        searcherService.search(queueMessageDto.getId());
    }
}