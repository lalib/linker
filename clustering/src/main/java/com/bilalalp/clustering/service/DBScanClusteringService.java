package com.bilalalp.clustering.service;

import com.bilalalp.common.entity.cluster.ClusteringRequestInfo;
import com.bilalalp.common.entity.tfidf.TfIdfRequestInfo;
import com.bilalalp.common.service.TfIdfRequestInfoService;
import de.jungblut.clustering.DBSCANClustering;
import de.jungblut.distance.EuclidianDistance;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DBScanClusteringService implements ClusteringService {

    @Autowired
    private TfIdfRequestInfoService tfIdfRequestInfoService;

    @Override
    public void cluster(final ClusteringRequestInfo clusteringRequestInfo) {

        final TfIdfRequestInfo tfIdfRequestInfo = tfIdfRequestInfoService.find(clusteringRequestInfo.getTfIdfRequestId());

        final String path = tfIdfRequestInfo.getFileName();
        final int numClusters = clusteringRequestInfo.getClusterNumber().intValue();

        final List<DoubleVector> clusteringVector = getClusteringVector(path);

        final EuclidianDistance measure = new EuclidianDistance();

        List<List<DoubleVector>> cluster = DBSCANClustering.cluster(clusteringVector, measure,
                5, 100d);

        System.out.println("geldi..");
    }

    private List<DoubleVector> getClusteringVector(final String filePath) {

        try (final Stream<String> stream = Files.lines(Paths.get(filePath))) {

            final List<DoubleVector> doubleVectors = new ArrayList<>();

            stream.forEach(s -> {

                final String[] split1 = s.split("::");
                final Double patentId = Double.valueOf(split1[0]);

                final String[] split = split1[1].split("\\$");
                for (int i = 0; i < split.length; i++) {
                    final String[] split2 = split[i].split(":");
                    doubleVectors.add(new DenseDoubleVector(new double[]{patentId, Double.valueOf(split2[0]), Double.valueOf(split2[1])}));
                }
            });

            return doubleVectors;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
