package com.android.androidtech.utils.dex;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by yuchengluo on 2015/10/30.
 */
public class ReflectUtil {

    public static boolean setObjectField(Object obj, String fieldName, Object newValue) {
        boolean result = false;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, newValue);
            result = true;
        } catch (Throwable e) {
            // e.printStackTrace();
        }

        return result;
    }

    public static Method getObjectMethod(Object obj, String methodName) {
        try {
            Method[] methods = obj.getClass().getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    if (method != null && method.getName().equals(methodName)) {
                        return method;
                    }
                }
            }
        } catch (Throwable e) {
        }

        return null;
    }

    public static Method getObjectMethodByArgs(Object instance, String name, Class... parameterTypes)
            throws NoSuchMethodException {
        Class clazz = instance.getClass();

        while (clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(name, parameterTypes);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                return method;
            } catch (NoSuchMethodException var5) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method " + name + " with parameters " + Arrays.asList(parameterTypes)
                + " not found in " + instance.getClass());
    }

    public static Field getObjectField(Object obj, String fieldName) {
        Field field = null;
        try {

            Class parentClass = obj.getClass();
            while (field == null) {
                try {
                    field = parentClass.getDeclaredField(fieldName);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                try {
                    parentClass = parentClass.getSuperclass();
                    if (parentClass == null) {
                        break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            if (field != null && !field.isAccessible()) {
                field.setAccessible(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return field;
    }

    public static Object getObjectFieldValue(Object obj, String fieldName) {
        Object result = null;
        try {
            Field field = getObjectField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                result = field.get(obj);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Object invoke(Object obj, String methodName, Object[] args) {
        Object result = null;
        try {
            Method method = getObjectMethod(obj, methodName);
            if (method != null) {
                method.setAccessible(true);
                result = method.invoke(obj, args);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }
}
