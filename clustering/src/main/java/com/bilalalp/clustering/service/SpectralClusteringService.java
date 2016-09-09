package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.commons.math3.util.Precision;
import org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
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

    public static void ma22n(String[] args) throws Exception {
        DistanceMeasure measure = new EuclideanDistanceMeasure();
        org.apache.hadoop.fs.Path output = new org.apache.hadoop.fs.Path("D:\\patentdoc\\output");
        org.apache.hadoop.fs.Path tempDir = new org.apache.hadoop.fs.Path("C:\\patentdoc\\temp");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        HadoopUtil.delete(conf, output);

        org.apache.hadoop.fs.Path affinities = new org.apache.hadoop.fs.Path("C:\\patentdoc\\bilal.txt");
        org.apache.hadoop.fs.FileSystem fs = org.apache.hadoop.fs.FileSystem.get(output.toUri(), conf);
        if (!fs.exists(output)) {
            fs.mkdirs(output);
        }

        final int datasize = 65868;

        int maxIter = 10;
        double convergenceDelta = 0.001;
        SpectralKMeansDriver.run(new org.apache.hadoop.conf.Configuration(), affinities, output, datasize, 3, measure, convergenceDelta, maxIter, tempDir);
        System.out.println("bitti");
    }

    public static void main12(String[] args) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get("D:\\patentdoc\\bilal-son.txt"), StandardCharsets.UTF_8);

        final Map<Long, Map<Long, Long>> longMapMap = getLongMapMap(lines);
        final Map<Long, Long> maxMap = findMaxValues(longMapMap);

        final Map<Long, Map<Long, Double>> longDoubleMap = new HashMap<>();

        for (final Map.Entry<Long, Map<Long, Long>> longMapEntry : longMapMap.entrySet()) {
            if (longMapEntry.getKey() == 62205L) {
                System.out.println("geldi..");
            }
            final Map<Long, Double> transformedMap = transform(longMapEntry.getValue(), maxMap.get(longMapEntry.getKey()));
            longDoubleMap.put(longMapEntry.getKey(), transformedMap);
        }

        for (final Map.Entry<Long, Map<Long, Double>> longMapEntry : longDoubleMap.entrySet()) {

            final StringBuilder stringBuilder = new StringBuilder();

            for (final Map.Entry<Long, Double> doubleEntry : longMapEntry.getValue().entrySet()) {
                double round = Precision.round(doubleEntry.getValue(), 1, 1);
                if (longMapEntry.getKey() == 62205L) {
                    System.out.println("geldi..");
                }
                final String value = longMapEntry.getKey() + " " + doubleEntry.getKey() + " " + round + "\n";
                stringBuilder.append(value);
            }
            Files.write(Paths.get("C:\\patentdoc\\bilal.txt"), Collections.singletonList(stringBuilder.toString().trim()), Charset.forName("UTF-8"), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        }

        System.out.println(longDoubleMap.size());
    }

    private static Map<Long, Double> transform(final Map<Long, Long> value, final Long aLong) {

        final Map<Long, Double> valueMap = new HashMap<>();
        for (final Map.Entry<Long, Long> doubleEntry : value.entrySet()) {
            final double val = doubleEntry.getValue().doubleValue() / aLong;
            final double result = 1d - val;
            if (result < 0) {
                System.out.println("geldi..");
            }
            if (aLong != 0L) {
                valueMap.put(doubleEntry.getKey(), result);
            } else {
                valueMap.put(doubleEntry.getKey(), 0d);
            }
        }
        return valueMap;
    }

    private static Map<Long, Long> findMaxValues(final Map<Long, Map<Long, Long>> longMapMap) {

        final Map<Long, Long> maxMap = new HashMap<>();
        for (final Map.Entry<Long, Map<Long, Long>> longLongEntry : longMapMap.entrySet()) {
            maxMap.put(longLongEntry.getKey(), getMaxValue(longLongEntry.getValue()));
        }

        return maxMap;
    }

    private static Long getMaxValue(final Map<Long, Long> value) {

        Long maxValue = 0L;
        Long maxKey = 0L;

        for (final Map.Entry<Long, Long> entry : value.entrySet()) {
            if (maxKey == 0L) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }

            if (maxValue < entry.getValue()) {
                maxValue = entry.getValue();
            }
        }

        return maxValue;
    }

    private static Map<Long, Map<Long, Long>> getLongMapMap(List<String> lines) {
        final Map<Long, Map<Long, Long>> longMapMap = new HashMap<>();

        for (final String value : lines) {
            final String[] split = value.split(" ");
            final Long key = Long.valueOf(split[0]);
            if (!longMapMap.containsKey(key)) {
                longMapMap.put(key, new HashMap<>());
            }

            longMapMap.get(key).put(Long.valueOf(split[1]), Long.valueOf(split[2]));
        }
        return longMapMap;
    }

    public static void main(String[] args) throws Exception {

        final String filePath = "C:\\patentdoc\\1070645540-300.txt";

        final Map<Long, Map<Long, Long>> longMapMap = getFileContent(filePath);

        final List<Map.Entry<Long, Map<Long, Long>>> randAccess = new ArrayList<>(longMapMap.entrySet());

        for (int i = 0; i < randAccess.size(); i++) {

            final StringBuilder stringBuilder = new StringBuilder();

            final Map.Entry<Long, Map<Long, Long>> longMapEntry = randAccess.get(i);
            final Long parentPatentId = longMapEntry.getKey();
            final Map<Long, Long> parentValue = longMapEntry.getValue();

            for (int j = i; j < randAccess.size(); j++) {
                final Map.Entry<Long, Map<Long, Long>> longMapEntry2 = randAccess.get(j);
                final Long aLong = calculateHammingDistance(parentValue, longMapEntry2.getValue());

                stringBuilder.append(parentPatentId).append(" ").append(longMapEntry2.getKey()).append(" ").append(aLong).append("\n");
            }

            writeToFile(stringBuilder.toString().trim());
        }

        System.out.println("bitti." + longMapMap);
    }

    private static void writeToFile(final String value) {

        try {

            String myPath = "D:\\patentdoc\\bilal-son24.txt";
            File file = new File(myPath);
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
