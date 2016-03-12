package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.service.PatentRowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class PatentFileRowMapperService {

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    public void insertPatentRowsToDatabase() {
        final String path = "C:\\patentdoc\\records.txt";
        final Long tfidfRequestId = 23333068L;


        try (final BufferedReader br = new BufferedReader(new FileReader(path))) {

            int k = 0;
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                k++;
                final String val = sCurrentLine.split("::")[0];

                final PatentRowInfo patentRowInfo = new PatentRowInfo();
                patentRowInfo.setPatentId(Long.valueOf(val));
                patentRowInfo.setRowNumber(k);
                patentRowInfo.setTfIdfRequestInfoId(tfidfRequestId);
                patentRowInfoService.saveInNewTransaction(patentRowInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}