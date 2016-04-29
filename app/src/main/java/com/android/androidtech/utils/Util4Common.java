package com.android.androidtech.utils;

import android.view.View;
import android.view.ViewParent;

import java.lang.reflect.Field;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class Util4Common {
    public static boolean findView(View viewParent, Object findView) {
        if (viewParent == null || findView == null) {
            return false;
        }

        if (findView == viewParent) {
            return true;
        }

        boolean result = false;
        if (findView instanceof View) {
            View view = (View) findView;
            ViewParent parent = view.getParent();
            while (parent != null) {
                if (parent == viewParent) {
                    result = true;
                    break;
                }

                parent = parent.getParent();
            }
        }
        return result;
    }
    public static Field getgetObjectField(Object obj, String fieldName){
        java.lang.reflect.Field field = null;
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
            java.lang.reflect.Field field = getgetObjectField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                result = field.get(obj);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }
    public static boolean setObjectField(Object obj, String fieldName, Object newValue) {
        boolean result = false;
        try {
            Field field = getgetObjectField(obj,fieldName);
            field.setAccessible(true);
            field.set(obj, newValue);
            result = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }
}
