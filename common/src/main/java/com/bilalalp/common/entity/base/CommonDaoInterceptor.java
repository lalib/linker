package com.bilalalp.common.entity.base;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

public class CommonDaoInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals("creationDate")) {
                state[i] = new Date();
            }
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {

        if (entity instanceof AbstractEntity) {
            final AbstractEntity abstractEntity = (AbstractEntity) entity;
            abstractEntity.setDateUpdated(new Date());
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
