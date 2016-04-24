package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.PatentRowInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

@Slf4j
@Service
public class PatentFileRowMapperService implements Serializable{

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    public void insertPatentRowsToDatabase(final ClusteringRequestInfo clusteringRequestInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        try (final BufferedReader br = new BufferedReader(new FileReader(tfIdfRequestInfo.getFileName()))) {

            int k = 0;
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                k++;

                final PatentRowInfo patentRowInfo = new PatentRowInfo();
                patentRowInfo.setPatentId(Long.valueOf(sCurrentLine.split("::")[0]));
                patentRowInfo.setRowNumber(k);
                patentRowInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());
                patentRowInfoService.saveInNewTransaction(patentRowInfo);
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}