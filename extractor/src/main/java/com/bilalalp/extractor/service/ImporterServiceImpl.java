package com.bilalalp.extractor.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.extractor.amqp.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImporterServiceImpl implements ImporterService {

    @Autowired
    private PatentInfoService patentInfoService;

    @Qualifier(value = "collectorQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Autowired
    private MessageSender messageSender;

    @Transactional
    @Override
    public void importToQueue(final Long requestId) {
        final List<Long> patentIds = patentInfoService.getPatentIds(requestId);
        final List<QueueMessageDto> queueMessageDtoList = patentIds.stream().map(QueueMessageDto::new).collect(Collectors.toList());
        messageSender.sendMessage(queueConfigurationDto, queueMessageDtoList);
    }
}