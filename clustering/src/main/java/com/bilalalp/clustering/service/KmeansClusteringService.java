package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.PatentRowInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;

@Service
public class KmeansClusteringService implements ClusteringService, Serializable {

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Override
    public void cluster(final ClusteringRequestInfo clusteringRequestInfo) {

        final SparkConf conf = new SparkConf().setAppName("K-gdfgdfg").setMaster("local[*]")
                .set("spark.executor.memory", "10g");
        final JavaSparkContext sc = new JavaSparkContext(conf);

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
//        final String path = "C:\\patentdoc\\1068405328-10000.txt";
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

//        final List<PatentRowInfo> all = patentRowInfoService.findAll();
//        final Map<Integer, Long> patentRowInfoMap = ClusterUtil.createRowInfoMap(all);

        final JavaRDD<String> data = sc.textFile(path);
//        final JavaRDD<Vector> parsedData = ClusterUtil.getVectorJavaRDD(data);
//        parsedData.cache();

        final JavaPairRDD<Long, Vector> javaPaidRDD = ClusterUtil.getJavaPaidRDD(data);

        javaPaidRDD.cache();

        final RDD<Vector> rdd = javaPaidRDD.values().rdd();


        final KMeansModel clusters = KMeans.train(rdd, numClusters, Integer.MAX_VALUE);
        final double wssse = clusters.computeCost(rdd);

        final JavaRDD<Tuple2<Long, Integer>> map = javaPaidRDD.map(t -> new Tuple2<>(t._1(), clusters.predict(t._2())));

        final Broadcast<TfIdfRequestInfo> tfIdfRequestInfoBroadcast = sc.broadcast(tfIdfRequestInfo);
        final Broadcast<ClusteringRequestInfo> clusteringRequestInfoBroadcast = sc.broadcast(clusteringRequestInfo);
        final Broadcast<Double> broadcast = sc.broadcast(wssse);

        final JavaRDD<ClusteringResultInfo> requestInfoJavaRDD = map.map(t -> {
            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
            clusteringResultInfo.setClusteringNumber((long) t._2());
            clusteringResultInfo.setPatentId(t._1());
            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfoBroadcast.getValue().getId());
            clusteringResultInfo.setWssse(broadcast.getValue());
            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfoBroadcast.getValue().getId());
            return clusteringResultInfo;
        });

        List<ClusteringResultInfo> collect = requestInfoJavaRDD.collect();
        System.out.println("geldi.");
        for (final ClusteringResultInfo clusteringResultInfo : collect) {
            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
        }

//        final KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, Integer.MAX_VALUE, 20);
//        final double wssse = clusters.computeCost(parsedData.rdd());


//        final JavaRDD<Integer> predict = clusters.predict(parsedData);
//        final List<Integer> clusterResults = predict.collect();

//        for (int i = 0; i < clusterResults.size(); i++) {
//            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
//            clusteringResultInfo.setClusteringNumber((long) (clusterResults.get(i) + 1));
//            clusteringResultInfo.setPatentId(patentRowInfoMap.get(i + 1));
//            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());
//            clusteringResultInfo.setWssse(wssse);
//            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfo.getId());
//            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
//        }
    }
}