package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.ClusterResultInfoService;
import com.bilalalp.common.service.PatentRowInfoService;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.clustering.LDAModel;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;

@Service
public class LatentDirichletAllocationClusteringService implements ClusteringService, Serializable {

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

//        final List<PatentRowInfo> all = patentRowInfoService.findAll();
//        final Map<Integer, Long> patentRowInfoMap = ClusterUtil.createRowInfoMap(all);

        final SparkConf conf = new SparkConf().setAppName("LDA").setMaster("local[4]").set("spark.executor.memory", "1g");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        final JavaRDD<String> data = sc.textFile(path);
        final JavaRDD<Vector> parsedData = ClusterUtil.getVectorJavaRDD(data);

        final JavaPairRDD<Long, Vector> corpus = JavaPairRDD
                .fromJavaRDD(parsedData.zipWithIndex()
                        .map((Function<Tuple2<Vector, Long>, Tuple2<Long, Vector>>) Tuple2::swap));

        corpus.cache();

        final LDAModel run = new LDA().setK(numClusters).run(corpus);
        final DistributedLDAModel ldaModel = (DistributedLDAModel) run;
        final RDD<Tuple2<Object, Vector>> tuple2RDD = ldaModel.topicDistributions();

        System.out.println("Learned topics (as distributions over vocab of " + ldaModel.vocabSize() + " words):");
        final Matrix topics = ldaModel.topicsMatrix();
        for (int topic = 0; topic < numClusters; topic++) {
            System.out.print("Topic " + topic + ":");
            for (int word = 0; word < ldaModel.vocabSize(); word++) {
                System.out.print(" " + topics.apply(word, topic));
            }
            System.out.println();
        }
    }

}