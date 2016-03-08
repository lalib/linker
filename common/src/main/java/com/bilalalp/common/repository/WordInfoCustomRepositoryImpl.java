package com.bilalalp.common.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class WordInfoCustomRepositoryImpl implements WordInfoCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createDictionary(final Long lsrId) {
        final String query = "INSERT INTO T_Word_info (id,c_version,c_word) SELECT (nextval('hibernate_sequence')),0,T.c_word FROM T_WORD_SUMMARY_INFO T WHERE T.C_LSR_ID = ? ";
        entityManager.createNativeQuery(query).setParameter(1, lsrId).executeUpdate();
    }
}
