package com.bilalalp.common.repository;


import com.bilalalp.common.entity.tfidf.TfIdfProcessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TfIdfProcessInfoRepository extends JpaRepository<TfIdfProcessInfo, Long> {
}
