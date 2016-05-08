package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class SpectralClusteringService implements ClusteringService {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Override
    public void cluster(final ClusteringRequestInfo clusteringRequestInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

    }

    public static void main(String[] args) throws IOException {
        List lines = Files.readAllLines(Paths.get("D:\\patentdoc\\bilal-son.txt"),
                StandardCharsets.UTF_8);

        System.out.println(lines.size());
    }

    public static void main1(String[] args) throws Exception {

        final String filePath = "C:\\patentdoc\\1068134912-100.txt";

        final Map<Long, Map<Long, Long>> longMapMap = getFileContent(filePath);

        final List<Map.Entry<Long, Map<Long, Long>>> randAccess = new ArrayList<>(longMapMap.entrySet());

        for (int i = 0; i < randAccess.size(); i++) {

            final StringBuilder stringBuilder = new StringBuilder();
            int testCount = 0;

            final Map.Entry<Long, Map<Long, Long>> longMapEntry = randAccess.get(i);
            final Long parentPatentId = longMapEntry.getKey();
            final Map<Long, Long> parentValue = longMapEntry.getValue();

            for (int j = i; j < randAccess.size(); j++) {
                final Map.Entry<Long, Map<Long, Long>> longMapEntry2 = randAccess.get(j);
                final Long aLong = calculateHammingDistance(parentValue, longMapEntry2.getValue());

                stringBuilder.append(parentPatentId).append(" ").append(longMapEntry2.getKey()).append(" ").append(aLong).append("\n");

                if (i != j) {
                    stringBuilder.append(longMapEntry2.getKey()).append(" ").append(parentPatentId).append(" ").append(aLong).append("\n");
                    testCount++;
                }
            }

            writeToFile(stringBuilder.toString().trim());
        }

        System.out.println("bitti." + longMapMap);
    }

    private static void writeToFile(final String value) {

        try {

            String myPath = "D:\\patentdoc\\bilal-son.txt";
            File file =  new File(myPath);
//            file.createNewFile();

            Files.write(Paths.get(myPath), Collections.singletonList(value), Charset.forName("UTF-8"), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static Long calculateHammingDistance(final Map<Long, Long> firstMap, final Map<Long, Long> secondMap) {

        Long calculatedValue = 0L;

        for (Map.Entry<Long, Long> longEntry : firstMap.entrySet()) {

            if (!Objects.equals(longEntry.getValue(), secondMap.get(longEntry.getKey()))) {
                calculatedValue++;
            }

        }
        return calculatedValue;
    }

    private static Map<Long, Map<Long, Long>> getFileContent(String filePath) throws IOException {
        final Map<Long, Map<Long, Long>> longMapMap = new ConcurrentHashMap<>();

        try (final Stream<String> stream = Files.lines(Paths.get(filePath))) {

            stream.forEach(s -> {

                final String[] split1 = s.split("::");
                final Long patentId = Long.valueOf(split1[0]);

                final String[] split = split1[1].split("\\$");
                for (String aSplit : split) {
                    final String[] split2 = aSplit.split(":");

                    if (!longMapMap.containsKey(patentId)) {
                        longMapMap.put(patentId, new HashMap<>());
                    }

                    longMapMap.get(patentId).put(Long.valueOf(split2[0]), Long.valueOf(split2[1]));
                }
            });
        }
        return longMapMap;
    }
}
