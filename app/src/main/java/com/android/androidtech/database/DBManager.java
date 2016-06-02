package com.android.androidtech.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.androidtech.database.tables.ContactInfoTable;
import com.android.androidtech.database.tables.DBConfig;
import com.android.androidtech.utils.GLog;


/**
 * * Copyright (C) 1998-2014 TENCENT Inc.All Rights Reserved.
 *
 * @author yuchengluo
 * @Description: DB管理类，主要为创建表及DB升级
 * @date 2013-2-15 modify
 */
public class DBManager implements DBConfig {
    private static final String TAG = "DBManager";
    private static SQLiteDatabase mDB = null;
    private static SQLiteDatabase mDBReadable = null;
    private static DatabaseHelper mDatabaseHelper = null;

    public static void close() {
        if (mDB != null) {
            try {
                mDB.close();
            } catch (Exception e) {
            }
            mDB = null;
            GLog.i(TAG, "[DBManager]close()");
        }
        if (mDBReadable != null) {
            try {
                mDBReadable.close();
            } catch (Exception e) {
            }
            mDBReadable = null;
            GLog.i(TAG, "[DBManager] mDBReadable close()");
        }
    }

    private static DatabaseHelper getDatabaseHelper(Context AppContex) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(AppContex);
        }
        return mDatabaseHelper;
    }

    /**
     * @param AppContext 这里的Context一定要用ApplicationContext 初始化DB
     * @return
     */
    public static synchronized void InitDB(Context AppContext) {
        // GLog.i(TAG, "getDB");
        // 暂时在启动初始化，如果初始化时间较长，在第一次使用初使化也是一种考虑方法
        getWriteDB(AppContext);
        getReadDB(AppContext);
    }

    /**
     * 获取写DB
     */
    public static synchronized SQLiteDatabase getWriteDB(Context AppContext) {
        // GLog.i(TAG, "getDB");
        if (mDB == null || !mDB.isOpen()) {
            mDB = getDatabaseHelper(AppContext).getWritableDatabase();
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//               // mDB.enableWriteAheaGLogging();
//                GLog.i(TAG, "[DBManager]getDB() enableWriteAheaGLogging");
//            }
            GLog.i(TAG, "[DBManager]getWriteDB()");
        }
        return mDB;
    }

    /**
     * 获取只读DBHelper
     */
    public static synchronized SQLiteDatabase getReadDB(Context AppContext) {
        // GLog.i(TAG, "getDB");
        if (mDBReadable == null || !mDBReadable.isOpen()) {
            mDBReadable = getDatabaseHelper(AppContext).getReadableDatabase();

            GLog.i(TAG, "[DBManager]getReadDB()");
        }
        return mDBReadable;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        // 这里的Context一定要用ApplicationContext
        private Context mContext = null;

        DatabaseHelper(Context context) {
            super(context, DATABASE_FILE, null, DBConfig.DB_VER);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 创建+读入歌曲数据库表
            GLog.i(TAG, "[DatabaseHelper]CreateDB :");
            // 创建表,如果有新增字段必须进入CreateTable里面来加入新的字段
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                GLog.i(TAG, "数据库降级 old:" + oldVersion + " newVersion:" + newVersion);

                //用来触发重新扫描
            } catch (Exception e) {
                GLog.e(TAG, e);
            }
        }

        private void createTable(SQLiteDatabase db) {
            //创建表
            db.execSQL("DROP TABLE IF EXISTS  " + ContactInfoTable.CONTACT_INFO_TABLE);
            //创建表中具体的字段
            db.execSQL(ContactInfoTable.TABLE_CREATE);
        }
    }
}

