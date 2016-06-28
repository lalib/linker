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
import org.apache.spark.mllib.clustering.GaussianMixture;
import org.apache.spark.mllib.clustering.GaussianMixtureModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.stat.distribution.MultivariateGaussian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class GaussianMixtureClusteringService implements ClusteringService {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Autowired
    private ClusterResultInfoService clusterResultInfoService;

    @Autowired
    private PatentRowInfoService patentRowInfoService;

    @Transactional
    @Override
    public void cluster(ClusteringRequestInfo clusteringRequestInfo) {

        final SparkConf conf = new SparkConf().setAppName("K-gdfgdfg").setMaster("local[*]")
                .set("spark.executor.memory", "6g");
        final JavaSparkContext sc = new JavaSparkContext(conf);

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final List<PatentRowInfo> all = patentRowInfoService.findAll();
        final Map<Integer, Long> patentRowInfoMap = ClusterUtil.createRowInfoMap(all);

        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Vector> parsedData = ClusterUtil.getVectorJavaRDD(data);

        parsedData.cache();

        final GaussianMixtureModel gmm = new GaussianMixture().setK(numClusters).run(parsedData.rdd());

        final MultivariateGaussian[] gaussians = gmm.gaussians();
        final double[] weights = gmm.weights();

        final JavaRDD<Integer> predict = gmm.predict(parsedData);
        final List<Integer> clusterResults = predict.collect();

        for (int i = 0; i < clusterResults.size(); i++) {
            final ClusteringResultInfo clusteringResultInfo = new ClusteringResultInfo();
            clusteringResultInfo.setClusteringNumber((long) (clusterResults.get(i) + 1));
            clusteringResultInfo.setPatentId(patentRowInfoMap.get(i + 1));
            clusteringResultInfo.setTfIdfRequestInfoId(tfIdfRequestInfo.getId());
            clusteringResultInfo.setClusteringRequestId(clusteringRequestInfo.getId());
            clusterResultInfoService.saveInNewTransaction(clusteringResultInfo);
        }
    }
}