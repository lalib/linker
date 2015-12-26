package com.bilalalp.exporter.service;

import com.bilalalp.common.entity.PatentInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchPageInfo;
import com.bilalalp.common.entity.linksearch.LinkSearchRequestInfo;
import com.bilalalp.common.repository.LinkSearchPageInfoRepository;
import com.bilalalp.common.repository.PatentInfoRepository;
import com.bilalalp.common.service.LinkSearchRequestInfoService;
import com.bilalalp.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ExporterServiceImpl implements ExporterService {

    private static final String COMMA_DELIMITER = "##$$##";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //TODO to be refactored.
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LinkSearchRequestInfoService linkSearchRequestInfoService;

    @Autowired
    private LinkSearchPageInfoRepository linkSearchPageInfoRepository;

    @Autowired
    private PatentInfoRepository patentInfoRepository;

    @Transactional
    public void export(final Long requestId) throws IOException {

        final LinkSearchRequestInfo linkSearchRequestInfo = linkSearchRequestInfoService.find(requestId);
        final List<LinkSearchPageInfo> linkSearchPageInfoList = linkSearchPageInfoRepository.getLinkSearchPageInfoListBylinkSearchRequestInfo(linkSearchRequestInfo.getId());

        final FileWriter fileWriter = new FileWriter("C:\\exportable\\testfile.csv");

        for (final LinkSearchPageInfo linkSearchPageInfo : linkSearchPageInfoList) {

            List<PatentInfo> patentInfos = patentInfoRepository.getPatentListBylinkSearchPageInfo(linkSearchPageInfo);

            for (final PatentInfo patentInfo : patentInfos) {

                fileWriter.append(patentInfo.getId().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getPatentNumber());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getPatentTitle());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getPatentLink());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getInventors());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getInternationalClass());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getPrimaryClass());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getAssignee());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(DateUtil.fromDate(patentInfo.getFillingDate(), DateUtil.DD_MM_YYYY));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(DateUtil.fromDate(patentInfo.getPublicationDate(), DateUtil.DD_MM_YYYY));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getAbstractContent());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getDescriptionContent());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(patentInfo.getClaimContent());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.flush();
            }

            entityManager.clear();

        }
        fileWriter.close();
    }
}