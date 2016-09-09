package com.bilalalp.clustering;

import com.bilalalp.common.service.PatentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FileTransformer2 {

    @Autowired
    private PatentInfoService patentInfoService;

    public void createFile() {

        final List<Long> patentIds = patentInfoService.getLatestPatentIds(5000L);


        for(int i=0;i<patentIds.size();i++){
            System.out.println(i);
            final Long patentId = patentIds.get(i);

            final List<Long> newPatentList = filterPatentList(patentIds,i);
            final Map<Long, Long> patentRelationMap = patentInfoService.getPatentRelationMap(patentId, newPatentList);
            writeToFile(getPatentRelationStr(patentId, patentRelationMap));
        }
    }

    private List<Long> filterPatentList(List<Long> patentIds, int i) {
        return patentIds.subList(i,patentIds.size());
    }

    private String getPatentRelationStr(Long patentId, Map<Long, Long> patentRelationMap) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<Long, Long> longLongEntry : patentRelationMap.entrySet()) {
            stringBuilder.append(patentId).append(" ").append(longLongEntry.getKey()).append(" ").append(longLongEntry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

    private void writeToFile(final String value) {

        try {

            File file = new File("D:\\patentdoc\\bilal-son3.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get("D:\\patentdoc\\bilal-son3.txt"), Collections.singletonList(value.trim()),
                    Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}