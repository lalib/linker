package com.bilalalp.entropyinitializer.service;

import com.bilalalp.common.dto.QueueMessageDto;
import com.bilalalp.common.entity.tfidf.TvProcessInfo;
import com.bilalalp.common.entity.tfidf.TvResultInfo;
import com.bilalalp.common.service.PatentInfoService;
import com.bilalalp.common.service.TvProcessInfoService;
import com.bilalalp.common.service.TvResultInfoService;
import com.bilalalp.common.service.WordSummaryInfoService;
import com.bilalalp.entropyinitializer.model.TfIdfProcessInfo;
import com.bilalalp.entropyinitializer.repo.TfRepository;
import com.mongodb.*;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamVarianceService implements Serializable, MessageListener {

    @Autowired
    private TvProcessInfoService tvProcessInfoService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private WordSummaryInfoService wordSummaryInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private TfRepository tfRepository;

    @Autowired
    private TvResultInfoService tvResultInfoService;

    final MongoClient mongoClient = new MongoClient();
    final DB mongoDatabase = mongoClient.getDB("tfidf");
    final DBCollection tfIdfProcessInfo = mongoDatabase.getCollection("tfIdfProcessInfo");

    public void mongoTest() {
        final TfIdfProcessInfo tfIdfProcessInfo = new TfIdfProcessInfo();
        tfIdfProcessInfo.setTfIdfValue(898d);
        tfIdfProcessInfo.setPatentId(98L);
        tfIdfProcessInfo.setWordId(989L);
        tfRepository.save(tfIdfProcessInfo);
    }

    public void process() {
        final long lsrId = 574L;

        final List<Long> patentIds = patentInfoService.getPatentIds(lsrId);
        final Long patentCount = (long) patentIds.size();

        for (int i = 0; i < patentIds.size(); i++) {

            final List<TvProcessInfo> tfResult = wordSummaryInfoService.getTfResult(patentIds.get(i), lsrId, patentCount);
            tvProcessInfoService.saveInNewTransaction(tfResult);
        }

        System.out.println("bitti.");
    }

    public void calculateTermVariance() {

        try {
            final List<BigDecimal> tvWordIds = wordSummaryInfoService.getTvWordIds();

            for (final BigDecimal wordId : tvWordIds) {

                final DBObject where = new BasicDBObject("wordId", wordId.toBigInteger().longValue());
                final DBCursor dbCursor = tfIdfProcessInfo.find(where).hint(new BasicDBObject("wordId", 1));
                final Iterator<DBObject> iterator = dbCursor.iterator();
                final List<DBObject> dbObjects = IteratorUtils.toList(iterator);

                final TvResultInfo tvResultInfo = new TvResultInfo();
                final Double result = getAvg(dbObjects);
                tvResultInfo.setAvg(result);
                Double diffSum = 0d;

                for (final DBObject dbObject : dbObjects) {
                    final Double tfIdfValue = (Double) dbObject.get("tfIdfValue");

                    final double diff = tfIdfValue - result;
                    diffSum += diff * diff;
                }

                tvResultInfo.setWordId(wordId.longValue());
                tvResultInfo.setTvResult(diffSum);
                tvResultInfoService.saveInNewTransaction(tvResultInfo);

                System.out.println(diffSum);
            }
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Double getAvg(final List<DBObject> dbObjects) {
        Double total = 0d;
        Long count = 0L;

        for (final DBObject dbObject : dbObjects) {
            final Double tfIdfValue = (Double) dbObject.get("tfIdfValue");
            total += tfIdfValue;
            count++;
        }

        return total / count;
    }

    @Override
    public void onMessage(Message message) {
        final QueueMessageDto queueMessageDto = (QueueMessageDto) messageConverter.fromMessage(message);
        System.out.println(queueMessageDto.getId());

        final List<TvProcessInfo> tfResult = wordSummaryInfoService.getTfResult(queueMessageDto.getId(), 574L, 65868L);
        final List<TfIdfProcessInfo> collect = tfResult.stream().map(k -> {
            final TfIdfProcessInfo tfIdfProcessInfo = new TfIdfProcessInfo();
            tfIdfProcessInfo.setWordId(k.getWordId());
            tfIdfProcessInfo.setPatentId(k.getPatentId());
            tfIdfProcessInfo.setTfIdfValue(k.getTfIdfValue());
            return tfIdfProcessInfo;
        }).collect(Collectors.toList());

        tfRepository.save(collect);
    }
}