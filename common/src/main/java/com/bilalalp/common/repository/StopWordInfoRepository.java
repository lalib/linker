package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.StopWordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopWordInfoRepository extends JpaRepository<StopWordInfo, Long> {

    StopWordInfo findByStopWord(final String stopWord);
}