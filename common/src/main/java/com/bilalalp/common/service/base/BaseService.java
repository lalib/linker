package com.bilalalp.common.service.base;

import java.util.Collection;

public interface BaseService<T> {

    void save(T t);

    void save(Collection<T> entityList);
}
