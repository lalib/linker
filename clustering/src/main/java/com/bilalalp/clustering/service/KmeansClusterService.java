package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusterResultInfo;
import com.bilalalp.common.entity.cluster.PatentRowInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.PatentRowInfoService;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KmeansClusterService implements ClusterService {

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Override
    public void cluster() {

        final String path = "C:\\patentdoc\\records.txt";
        final int numClusters = 20;
        final Long tfidfRequestId = 23333068L;

        final List<PatentRowInfo> all = patentRowInfoService.findAll();
        final Map<Integer, Long> patentRowInfoMap = createRowInfoMap(all);
        final SparkConf conf = new SparkConf().setAppName("K-means Example").setMaster("local[4]").set("spark.executor.memory", "1g");
        final JavaSparkContext sc = new JavaSparkContext(conf);

        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Vector> parsedData = data.map(
                (Function<String, Vector>) s -> {
                    final String[] split = s.split("::")[1].split("\\$");
                    double[] values = new double[split.length];
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

            final ClusterResultInfo clusterResultInfo = new ClusterResultInfo();
            clusterResultInfo.setClusterNumber((long) (clusterResults.get(i) + 1));
            clusterResultInfo.setPatentId(patentRowInfoMap.get(i + 1));
            clusterResultInfo.setTfIdfRequestInfoId(tfidfRequestId);
            clusterResultInfo.setWssse(wssse);
            clusterResultInfoService.saveInNewTransaction(clusterResultInfo);
        }
    }

    private Map<Integer, Long> createRowInfoMap(final List<PatentRowInfo> all) {
        return all.stream().collect(Collectors.toMap(PatentRowInfo::getRowNumber, PatentRowInfo::getPatentId));
    }
}