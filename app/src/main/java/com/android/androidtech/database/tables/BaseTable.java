package com.android.androidtech.database.tables;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.androidtech.database.DBManager;
import com.android.androidtech.utils.GLog;


/**
 *  基于SqliteDataBase存储的数据抽象表
 * Created by yuchengluo on 2015/6/29.
 */
public abstract class BaseTable {
    /**
     * 上下文指针
     */
    private Context mContext;

    /**
     * 构造函数
     *
     * @param context
     */
    public BaseTable(Context context) {
        mContext = context;
    }

    /**
     * 获取上下文指针
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 设置上下文指针
     *
     * @param context
     */
    public void setContext(Context context) {
        this.mContext = context;

    }

    /**
     * 获取ContentResolver
     *
     * @return
     */
    public ContentResolver getContentResolver() {
        return (mContext == null) ? null : mContext.getContentResolver();
    }

    /**
     * 获取表所属数据库
     *
     * @return
     */
    public SQLiteDatabase getSqliteDB() {
        return DBManager.getWriteDB(mContext.getApplicationContext());
    }

    /**
     * 获取表读数据库
     *
     * @return
     */
    public SQLiteDatabase getSqliteReadDB() {
        return DBManager.getReadDB(mContext.getApplicationContext());
    }
    /**
     * 获取SQL STATEMENT
     * */
    public abstract SQLiteStatement getSQLiteStatement();
    /**
     * 获取表名
     *
     * @return
     */
    public abstract String getTableName();
    /*
    * 获取所有键名
    * */
    public abstract String[] getAllKey();
    /**
     * @author kenzhang 查询表中元素数量,比原有的cursor.getCount()更高效
     * @return
     */
    public int getTotalCount() {
        Cursor cursor = null;
        try {
            SQLiteDatabase sqliteDb = getSqliteDB();
            if (sqliteDb != null) {
                cursor = sqliteDb.rawQuery("select count(*) from " + getTableName(), null);
                if (cursor != null && cursor.moveToFirst()) {
                    int i = cursor.getInt(0);
                    return i;
                }
            }
        } catch (Exception e) {
            GLog.e("BaseTable", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return -1;
    }

    protected static String kv(String key, String value) {
        return key + "=" + value;
    }

    protected static String kv(String key, long value) {
        return key + "=" + value;
    }

    protected static String kv(String key, int value) {
        return key + "=" + value;
    }
}
