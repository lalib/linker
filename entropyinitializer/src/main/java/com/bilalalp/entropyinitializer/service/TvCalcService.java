package com.bilalalp.entropyinitializer.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.tfidf.TvResultInfo;
import com.bilalalp.common.service.TvResultInfoService;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TvCalcService implements MessageListener {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private TvResultInfoService tvResultInfoService;

    final MongoClient mongoClient = new MongoClient();
    final MongoDatabase mongoDatabase = mongoClient.getDatabase("tfidf");
    final MongoCollection<Document> tfIdfProcessInfo = mongoDatabase.getCollection("tfIdfProcessInfo");

    @Override
    public void onMessage(final Message message) {

        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);

        final Long wordId = queueMessageDto.getId();
        final Document where = new Document("wordId", wordId);

        final FindIterable<Document> documents = tfIdfProcessInfo.find(where);
        final MongoCursor<Document> iterator = documents.iterator();
        final List<Document> dbObjects = iteratorToList(iterator);

        final TvResultInfo tvResultInfo = new TvResultInfo();
        final Double result = getAvg(dbObjects);
        tvResultInfo.setAvg(result);
        Double diffSum = 0d;

        for (final Document dbObject : dbObjects) {
            final Double tfIdfValue = (Double) dbObject.get("tfIdfValue");

            final double diff = tfIdfValue - result;
            diffSum += diff * diff;
        }

        tvResultInfo.setWordId(wordId);
        tvResultInfo.setTvResult(diffSum);
        tvResultInfoService.saveInNewTransaction(tvResultInfo);

        System.out.println(diffSum);
    }

    private Double getAvg(final List<Document> dbObjects) {
        Double total = 0d;
        Long count = 0L;

        for (final Document dbObject : dbObjects) {
            final Double tfIdfValue = (Double) dbObject.get("tfIdfValue");
            total += tfIdfValue;
            count++;
        }

        return total / count;
    }

    private List<Document> iteratorToList(final MongoCursor<Document> iterator) {
        final List<Document> dbObjects = new ArrayList<>();
        while (iterator.hasNext()) {
            dbObjects.add(iterator.next());
        }
        return dbObjects;
    }
}
