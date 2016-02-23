package com.bilalalp.common.repository;

import com.bilalalp.common.entity.patent.WordElimination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordEliminationRepository extends CrudRepository<WordElimination, Long> {
}