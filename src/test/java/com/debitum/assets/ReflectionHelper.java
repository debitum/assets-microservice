package com.debitum.assets;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;

public class ReflectionHelper {

    public static <T> T instance(Class<T> clazz) {
        try {
            Constructor declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return (T) declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object object,
                                     String fieldName,
                                     Object fieldValue) {
        try {
            FieldUtils.writeField(
                    object,
                    fieldName,
                    fieldValue,
                    true
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getFieldValue(Object object,
                                      String fieldName) {
        try {
            return (T) FieldUtils.readDeclaredField(
                    object,
                    fieldName,
                    true
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}