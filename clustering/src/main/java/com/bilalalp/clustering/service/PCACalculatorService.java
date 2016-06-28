package com.bilalalp.clustering.service;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


public class PCACalculatorService implements Serializable{

    public static void main(String[] args) {
//        final SparkConf conf = new SparkConf().setAppName("PCA Example");
//        final SparkContext sc = new SparkContext(conf);
//
//        final List<String> lines = Files.readAllLines(Paths.get("D:\\patentdoc\\bilal-son.txt"), StandardCharsets.UTF_8);
//
//        final LinkedList<Vector> rowsList = new LinkedList<>();
//        for (int i = 0; i < array.length; i++) {
//            Vector currentRow = Vectors.dense(array[i]);
//            rowsList.add(currentRow);
//        }
//        final JavaRDD<Vector> rows = JavaSparkContext.fromSparkContext(sc).parallelize(rowsList);
//
//        final RowMatrix mat = new RowMatrix(rows.rdd());
//
//        final Matrix pc = mat.computePrincipalComponents(3);
//        final  RowMatrix projected = mat.multiply(pc);
    }
}