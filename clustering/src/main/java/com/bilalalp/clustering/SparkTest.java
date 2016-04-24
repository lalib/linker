package com.bilalalp.clustering;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.util.List;

public class SparkTest {

    public static void main(String[] args) {

        final SparkConf conf = new SparkConf().setAppName("K-means123").setMaster("local[*]").set("spark.executor.memory", "6g");
        final JavaSparkContext sc = new JavaSparkContext(conf);
        final JavaRDD<String> data = sc.textFile("C:\\patentdoc\\1065738446-100.txt");

        final JavaRDD<Vector> parsedData = data.map((Function<String, Vector>) s -> {
            final String[] split = s.split("::")[1].split("\\$");
            final double[] values = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                values[i] = Double.parseDouble(split[i].split(":")[1]);
            }
            return Vectors.dense(values);
        });

        parsedData.cache();

        parsedData.rdd().collect();

        final KMeansModel clusters = KMeans.train(parsedData.rdd(), 2, 20);
        final double wssse = clusters.computeCost(parsedData.rdd());

        final JavaRDD<Integer> predict = clusters.predict(parsedData);
        final List<Integer> clusterResults = predict.collect();

        System.out.println("bitti.");
    }
}
