package com.bilalalp.entropyinitializer.repo;

import com.bilalalp.entropyinitializer.model.TfIdfProcessInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TfRepository extends MongoRepository<TfIdfProcessInfo, String> {

}