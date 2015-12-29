package com.bilalalp.extractor.service;

import com.bilalalp.common.dto.QueueConfigurationDto;
import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.patent.PatentClassInfo;
import com.bilalalp.common.entity.patent.PatentClassInfoType;
import com.bilalalp.common.entity.patent.PatentInfo;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.extractor.amqp.MessageSender;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public abstract class AbstractExtractorService implements ExtractorService {

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private MessageSender messageSender;

    @Qualifier("extractorQueueConfiguration")
    @Autowired
    private QueueConfigurationDto queueConfigurationDto;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void parse(final PatentInfo patentInfo) {

        final String body = patentInfo.getBody();
        if(StringUtils.isEmpty(body)){
            return;
        }
        final Document documentBody = Jsoup.parse(body);
        final String abstractContent = getAbstractContent(documentBody);
        final String claimContent = getClaimContent(documentBody);
        final String descriptionContent = getDescriptionContent(documentBody);
        final String inventors = getInventors(documentBody);
        final String assignee = getAssignee(documentBody);
        final String primaryClass = getPrimaryClass(documentBody);
        final Date publicationDate = getPublicationDate(documentBody);
        final String internationalClass = getInternationalClass(documentBody);
        final Date fillingDate = getFillingDate(documentBody);
        final String applicationNumber = getApplicationNumber(documentBody);
        final String patentNumber = getPatentNumber(documentBody);

        final List<PatentClassInfo> primaryClassPatentClassInfoList = createPatentClassInfo(primaryClass, PatentClassInfoType.PRIMARY, patentInfo);
        final List<PatentClassInfo> internationalClassPatentClassInfoList = createPatentClassInfo(internationalClass, PatentClassInfoType.INTERNATIONAL, patentInfo);
        final List<PatentClassInfo> patentClassInfoList = new ArrayList<>();
        patentClassInfoList.addAll(primaryClassPatentClassInfoList);
        patentClassInfoList.addAll(internationalClassPatentClassInfoList);

        patentInfo.setPatentClassInfoList(patentClassInfoList);
        patentInfo.setApplicationNumber(applicationNumber);
        patentInfo.setFillingDate(fillingDate);
        patentInfo.setPublicationDate(publicationDate);
        patentInfo.setAssignee(assignee);
        patentInfo.setAbstractContent(abstractContent);
        patentInfo.setClaimContent(claimContent);
        patentInfo.setDescriptionContent(descriptionContent);
        patentInfo.setInventors(inventors);
        patentInfo.setPatentNumber(patentNumber);
        patentInfo.setParsed(true);

        patentInfoService.save(patentInfo);
        messageSender.sendMessage(queueConfigurationDto, new QueueMessageDto(patentInfo.getId()));
    }

    private List<PatentClassInfo> createPatentClassInfo(final String primaryClass, final PatentClassInfoType primary, final PatentInfo patentInfo) {

        if (StringUtils.isNotEmpty(primaryClass)) {
            final String[] split = primaryClass.split(";");
            final Set<String> mySet = new HashSet<>(Arrays.asList(split));
            final List<PatentClassInfo> patentClassInfoList = new ArrayList<>();

            for (final String value : mySet) {
                final PatentClassInfo patentClassInfo = new PatentClassInfo();
                patentClassInfo.setPatentInfo(patentInfo);
                patentClassInfo.setClassInfo(value.trim().toLowerCase());
                patentClassInfo.setPatentClassInfoType(primary);
                patentClassInfoList.add(patentClassInfo);
            }

            return patentClassInfoList;
        } else {
            return new ArrayList<>();
        }
    }

    protected abstract String getInternationalClass(Document document);

    protected abstract String getPrimaryClass(Document document);

    protected abstract Date getPublicationDate(Document document);

    protected abstract Date getFillingDate(Document document);

    protected abstract String getAssignee(Document document);

    protected abstract String getApplicationNumber(Document document);

    protected abstract String getInventors(Document document);

    protected abstract String getAbstractContent(Document documentBody);

    protected abstract String getClaimContent(Document document);

    protected abstract String getDescriptionContent(Document document);

    protected abstract String getPatentNumber(Document document);
}