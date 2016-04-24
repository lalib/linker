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
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class KmeansClusteringService implements ClusteringService, Serializable {

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    final SparkConf conf = new SparkConf().setAppName("K-gdfgdfg").setMaster("local[*]")
            .set("spark.executor.memory", "6g");
    final JavaSparkContext sc = new JavaSparkContext(conf);

    @Override
    public void cluster(final ClusteringRequestInfo clusteringRequestInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final List<PatentRowInfo> all = patentRowInfoService.findAll();
        final Map<Integer, Long> patentRowInfoMap = ClusterUtil.createRowInfoMap(all);

        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Vector> parsedData = ClusterUtil.getVectorJavaRDD(data);

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
}