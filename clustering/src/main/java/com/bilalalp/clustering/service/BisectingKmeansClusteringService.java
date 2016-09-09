package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.mllib.clustering.BisectingKMeans;
import org.apache.spark.mllib.clustering.BisectingKMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;

@Service
public class BisectingKmeansClusteringService implements ClusteringService, Serializable {

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Override
    public void cluster(final ClusteringRequestInfo clusteringRequestInfo) {

        final SparkConf conf = new SparkConf().setAppName("Bisecting-kmeans").setMaster("local[*]")
                .set("spark.executor.memory", "10g");
        final JavaSparkContext sc = new JavaSparkContext(conf);

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final JavaRDD<String> data = sc.textFile(path);
        final JavaPairRDD<Long, Vector> javaPaidRDD = ClusterUtil.getJavaPaidRDD(data);

        javaPaidRDD.cache();

        final RDD<Vector> rdd = javaPaidRDD.values().rdd();

        BisectingKMeans bkm = new BisectingKMeans();
        BisectingKMeansModel model = bkm.setK(numClusters).run(rdd);

        final double wssse = model.computeCost(rdd);

        final JavaRDD<Tuple2<Long, Integer>> map = javaPaidRDD.map(t -> new Tuple2<>(t._1(), model.predict(t._2())));

        final Broadcast<TfIdfRequestInfo> tfIdfRequestInfoBroadcast = sc.broadcast(tfIdfRequestInfo);
        final Broadcast<ClusteringRequestInfo> clusteringRequestInfoBroadcast = sc.broadcast(clusteringRequestInfo);
        final Broadcast<Double> broadcast = sc.broadcast(wssse);

        final JavaRDD<ClusteringResultInfo> requestInfoJavaRDD = map.map(t -> {
            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
            clusteringResultInfo.setClusteringNumber((long) t._2() + 1);
            clusteringResultInfo.setPatentId(t._1());
            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfoBroadcast.getValue().getId());
            clusteringResultInfo.setWssse(broadcast.getValue());
            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfoBroadcast.getValue().getId());
            return clusteringResultInfo;
        });

        final List<ClusteringResultInfo> collect = requestInfoJavaRDD.collect();
        for (final ClusteringResultInfo clusteringResultInfo : collect) {
            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
        }
        System.out.println("geldi..");
    }
}