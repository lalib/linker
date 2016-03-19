package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.PatentRowInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KmeansClusteringService implements ClusteringService, Serializable {

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Override
    public void cluster(ClusteringRequestInfo clusteringRequestInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final List<PatentRowInfo> all = patentRowInfoService.findAll();
        final Map<Integer, Long> patentRowInfoMap = createRowInfoMap(all);
        final SparkConf conf = new SparkConf().setAppName("K-means").setMaster("local[4]").set("spark.executor.memory", "1g");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Vector> parsedData = data.map((Function<String, Vector>) s -> {
            final String[] split = s.split("::")[1].split("\\$");
            final double[] values = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                values[i] = Double.parseDouble(split[i].split(":")[1]);
            }
            return Vectors.dense(values);
        });

        parsedData.cache();

        final KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, Integer.MAX_VALUE);
        final double wssse = clusters.computeCost(parsedData.rdd());

        final JavaRDD<Integer> predict = clusters.predict(parsedData);
        final List<Integer> clusterResults = predict.collect();

        for (int i = 0; i < clusterResults.size(); i++) {
            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
            clusteringResultInfo.setClusteringNumber((long) (clusterResults.get(i) + 1));
            clusteringResultInfo.setPatentId(patentRowInfoMap.get(i + 1));
            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());
            clusteringResultInfo.setWssse(wssse);
            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfo.getId());
            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
        }
    }

    private Map<Integer, Long> createRowInfoMap(final List<PatentRowInfo> all) {
        return all.stream().collect(Collectors.toMap(PatentRowInfo::getRowNumber, PatentRowInfo::getPatentId));
    }
}