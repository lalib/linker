package com.bilalalp.dispatcher.service;

import com.bilalalp.dispatcher.amqp.MessageSender;
import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;
import com.bilalalp.dispatcher.dto.QueueConfigurationDto;
import com.bilalalp.dispatcher.dto.QueueMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DispatcherServiceImpl implements DispatcherService {

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "dispatcherRequestQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Override
    @Transactional
    public LinkSearchResponse processLinkSearchRequest(final LinkSearchRequest linkSearchRequest) {

        final QueueMessageDto queueMessageDto = new QueueMessageDto();
        queueMessageDto.setMessage("selam");
        messageSender.sendMessage(queueConfigurationDto, queueMessageDto);
        return new LinkSearchResponse();
    }
}