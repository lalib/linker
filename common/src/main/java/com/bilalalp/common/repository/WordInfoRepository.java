package com.bilalalp.common.repository;

import com.bilalalp.common.entity.tfidf.WordInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordInfoRepository extends CrudRepository<WordInfo, Long> {

    WordInfo findByWord(String word);
}