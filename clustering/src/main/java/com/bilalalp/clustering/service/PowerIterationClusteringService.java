package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.cluster.ClusteringResultInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.PowerIterationClustering;
import org.apache.spark.mllib.clustering.PowerIterationClusteringModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple3;

import java.io.Serializable;
import java.util.List;

@Service
public class PowerIterationClusteringService implements ClusteringService, Serializable {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Override
    public void cluster(ClusteringRequestInfo clusteringRequestInfo) {

        final SparkConf conf = new SparkConf().setAppName("K-gdfgdfg").setMaster("local[*]")
                .set("spark.executor.memory", "6g");
        final JavaSparkContext sc = new JavaSparkContext(conf);

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Tuple3<Long, Long, Double>> similarities = data.map(
                (Function<String, Tuple3<Long, Long, Double>>) line -> {
                    String[] parts = line.split(" ");
                    return new Tuple3<>(new Long(parts[0]), new Long(parts[1]), new Double(parts[2]));
                }
        );

        final PowerIterationClustering pic = new PowerIterationClustering()
                .setK(numClusters)
                .setMaxIterations(20);
        final PowerIterationClusteringModel model = pic.run(similarities);

        final List<PowerIterationClustering.Assignment> clusterResults = model.assignments().toJavaRDD().collect();


        for (int i = 0; i < clusterResults.size(); i++) {
            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
            clusteringResultInfo.setClusteringNumber(Long.valueOf(clusterResults.get(i).cluster()));
            clusteringResultInfo.setPatentId((clusterResults.get(i).id()));
            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());
            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfo.getId());
            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
        }

//
//        for (PowerIterationClustering.Assignment a: collect) {
//            System.out.println(a.id() + " -> " + a.cluster());
//        }

        System.out.println("geldi.");
    }
}

