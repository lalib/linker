package com.bilalalp.common.service.base;

import lombok.Setter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Setter
public abstract class AbstractService<T> implements BaseService<T> {

    protected abstract CrudRepository<T, Long> getRepository();

    @Transactional
    @Override
    public void save(final T entity) {
        getRepository().save(entity);
    }

    @Transactional
    @Override
    public void save(final Collection<T> entityList) {
        getRepository().save(entityList);
    }
}
