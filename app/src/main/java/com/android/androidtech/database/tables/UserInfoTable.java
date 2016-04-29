package com.android.androidtech.database.tables;

import android.content.Context;

/**
 * Created by yuchengluo on 2015/6/29.
 */
public class UserInfoTable extends BaseTable {
    private static String TAG = "UserInfoTable";
    public static final String USER_INFO_TABLE = "UserInfo_table"; // 账户信息table
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PHONE_NUM = "user_phonenumber";

    public static final String TABLE_CREATE = "create table if not exists " + USER_INFO_TABLE + " (" + KEY_USER_ID
            + " LONG primary key , " + KEY_USER_NAME + " TEXT," + KEY_USER_PHONE_NUM + " TEXT "
            + ");";

    /**
     * 构造函数
     *
     * @param context
     */
    public UserInfoTable(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return USER_INFO_TABLE;
    }

    @Override
    public String[] getAllKey() {
        return new String[]{
                USER_INFO_TABLE + "." + KEY_USER_ID,
                USER_INFO_TABLE + "." + KEY_USER_NAME,
                USER_INFO_TABLE + "." + KEY_USER_PHONE_NUM
        };
    }
}
