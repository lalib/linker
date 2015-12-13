package com.bilalalp.collector.service;

import com.bilalalp.collector.amqp.MessageSender;
import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.util.JSoupUtil;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectorServiceImpl implements CollectorService {

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier(value = "collectorQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void collect(final PatentInfo patentInfo) {

        final Element body = JSoupUtil.getBody(patentInfo.getPatentLink());

        final String endStr = "We are sorry but we experience a high volume traffic and we need to filter out the automatic queries form the legitimate human requests.";
        if (!(body == null || body.html().contains(endStr) || body.html().contains("null. (nullnull) "))) {

            patentInfo.setBody(getBody(body.html()));
            patentInfoService.save(patentInfo);
            messageSender.sendMessage(queueConfigurationDto, createQueueMessageDto(patentInfo));
        }
    }

    private String getBody(final String text){

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();

    }

    private QueueMessageDto createQueueMessageDto(final PatentInfo patentInfo) {
        return new QueueMessageDto(patentInfo.getId());
    }


}

