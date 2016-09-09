package com.bilalalp.common.repository;

import com.bilalalp.common.entity.AdditionalWordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalWordInfoRepository extends JpaRepository<AdditionalWordInfo, Long> {

    AdditionalWordInfo findByWord(String word);
}
