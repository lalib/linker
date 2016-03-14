package com.bilalalp.common.service.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseService<T> extends Serializable {

    void save(T t);

    void save(Collection<T> entityList);

    T find(Long id);

    List<T> findAll();
}