package com.bilalalp.common.service.base;

import lombok.Setter;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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

    @Transactional
    @Override
    public T find(final Long id) {
        return getRepository().findOne(id);
    }

    @Transactional
    @Override
    public List<T> findAll() {
        return IteratorUtils.toList(getRepository().findAll().iterator());
    }
}