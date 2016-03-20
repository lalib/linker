package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.PatentRowInfo;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ClusterUtil {

    public static JavaRDD<Vector> getVectorJavaRDD(JavaRDD<String> data) {
        return data.map((Function<String, Vector>) s -> {
            final String[] split = s.split("::")[1].split("\\$");
            final double[] values = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                values[i] = Double.parseDouble(split[i].split(":")[1]);
            }
            return Vectors.dense(values);
        });
    }

    public static Map<Integer, Long> createRowInfoMap(final List<PatentRowInfo> all) {
        return all.stream().collect(Collectors.toMap(PatentRowInfo::getRowNumber, PatentRowInfo::getPatentId));
    }
}
