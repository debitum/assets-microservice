package com.debitum.assets.port.adapter.helpers;

import org.apache.commons.lang3.Validate;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public interface HibernateHelper {

    static <T> T cast(Object object) {
        return (T) initializeAndUnproxy(object);
    }

    static <T, Y extends T> Y initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new
                    NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (Y) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return (Y) entity;
    }

    static boolean isAssignable(Object entity,
                                Class clazzToAssignTo) {
        Validate.notNull(clazzToAssignTo);
        if (entity == null) {
            return false;
        } else {
            return clazzToAssignTo.isAssignableFrom(Hibernate.getClass(entity));
        }
    }
}