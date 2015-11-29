package com.bilalalp.parser.consumer;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.site.SiteInfoType;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.parser.service.ParserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParserConsumer implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<SiteInfoType, ParserService> PARSER_SERVICE_MAP = new HashMap<>();

    @Transactional
    @Override
    public void onMessage(final Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId().toString());
        final PatentInfo patentInfo = patentInfoService.find(queueMessageDto.getId());
        PARSER_SERVICE_MAP.get(patentInfo.getLinkSearchPageInfo().getSiteInfoType()).parse(patentInfo);
    }

    @PostConstruct
    public void init() {
        final Collection<ParserService> values = applicationContext.getBeansOfType(ParserService.class).values();
        for (final ParserService searcherService : values) {
            PARSER_SERVICE_MAP.put(searcherService.getSiteInfoType(), searcherService);
        }
    }
}