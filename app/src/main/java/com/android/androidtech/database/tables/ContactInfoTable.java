package com.android.androidtech.database.tables;

import android.content.ContentValues;
import android.content.Context;

import com.android.androidtech.business.contact.ContactInfo;

/**
 * Created by yuchengluo on 2015/6/29.
 */
public class ContactInfoTable extends BaseTable {
    private static String TAG = "ContactInfoTable";
    public static final String CONTACT_INFO_TABLE = "ContactInfo_table"; // 账户信息table
    private static final String KEY_CONTACT_ID = "user_num";
    private static final String KEY_CONTACT_NAME = "user_name";
    private static final String KEY_USER_PHONE_NUM = "user_phonenumber";

    public static final String TABLE_CREATE = "create table if not exists " + CONTACT_INFO_TABLE + " (" + KEY_CONTACT_ID
            + " LONG primary key , " + KEY_CONTACT_NAME + " TEXT," + KEY_USER_PHONE_NUM + " LONG "
            + ");";

    /**
     * 构造函数
     *
     * @param context
     */
    public ContactInfoTable(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return CONTACT_INFO_TABLE;
    }

    @Override
    public String[] getAllKey() {
        return new String[]{
                CONTACT_INFO_TABLE + "." + KEY_CONTACT_ID,
                CONTACT_INFO_TABLE + "." + KEY_CONTACT_NAME,
                CONTACT_INFO_TABLE + "." + KEY_USER_PHONE_NUM
        };
    }
    public boolean insertContactInfo(ContactInfo info){
        return getSqliteDB().insert(CONTACT_INFO_TABLE,null,transContactInfo(info)) > 0;
    }
    public boolean updateContactInfo(ContactInfo info){
        return getSqliteDB().update(CONTACT_INFO_TABLE,transContactInfo(info),kv(KEY_CONTACT_ID,info.getContactId()),null) > 0;
    }
    public boolean deleteContactInfo(ContactInfo info){
        return getSqliteDB().delete(CONTACT_INFO_TABLE,kv(KEY_CONTACT_ID,info.getContactId()),null) > 0;
    }
    private ContentValues transContactInfo(ContactInfo info) {
        ContentValues value = new ContentValues();
        value.put(KEY_CONTACT_ID,info.getContactId());
        value.put(KEY_CONTACT_NAME,info.getContactName());
        value.put(KEY_USER_PHONE_NUM,info.getContactNum());
        return value;
    }
}
