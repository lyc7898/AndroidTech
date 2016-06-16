package com.android.androidtech.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.android.androidtech.business.contact.ContactInfo;

import java.util.ArrayList;

/**
 * Created by yuchengluo on 2015/6/29.
 */
public class ContactInfoTable extends BaseTable {
    private static String TAG = "ContactInfoTable";
    public static final String CONTACT_INFO_TABLE = "ContactInfo_table"; // 账户信息table
    private static final String KEY_CONTACT_ID = "contack_num";
    private static final String KEY_CONTACT_NAME = "contack_name";
    private static final String KEY_USER_PHONE_NUM = "contack_phonenumber";
    private SQLiteStatement mSQLiteStatement = null;
    public static final String TABLE_CREATE = "create table if not exists " + CONTACT_INFO_TABLE + " (" + KEY_CONTACT_ID
            + " LONG primary key , " + KEY_CONTACT_NAME + " TEXT," + KEY_USER_PHONE_NUM + " TEXT "
            + ");";
    public final String STR_INSERT_STATEMENT_CONTACTS = "insert into " + CONTACT_INFO_TABLE
            + "(" + KEY_CONTACT_ID
            + "," + KEY_CONTACT_NAME
            + "," + KEY_USER_PHONE_NUM
            + ") values(?,?,?)";

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

    @Override
    public SQLiteStatement getSQLiteStatement() {
        if (null == mSQLiteStatement) {
            mSQLiteStatement = getSqliteDB().compileStatement(STR_INSERT_STATEMENT_CONTACTS);
        }
        return mSQLiteStatement;
    }

    public boolean insertContactInfoForStat(ContactInfo info) {
        getSQLiteStatement().clearBindings();
        getSQLiteStatement().bindLong(1, info.getContactId());
        getSQLiteStatement().bindString(2, info.getContactName());
        getSQLiteStatement().bindString(3, info.getContactNum());
        return getSQLiteStatement().executeInsert() > 0;
    }

    public boolean insertContactInfo(ContactInfo info) {
        return getSqliteDB().insert(CONTACT_INFO_TABLE, null, transContactInfo(info)) > 0;
    }

    public boolean updateContactInfo(ContactInfo info) {
        return getSqliteDB().update(CONTACT_INFO_TABLE, transContactInfo(info), kv(KEY_CONTACT_ID, info.getContactId()), null) > 0;
    }

    public boolean deleteContactInfo(ContactInfo info) {
        return getSqliteDB().delete(CONTACT_INFO_TABLE, kv(KEY_CONTACT_ID, info.getContactId()), null) > 0;
    }

    public ArrayList<ContactInfo> getAllContacts() {
        ArrayList<ContactInfo> songList = new ArrayList<ContactInfo>();
        Cursor cursors = null;
        cursors = getSqliteDB().query(true,
                CONTACT_INFO_TABLE,
                new String[]{KEY_CONTACT_ID, KEY_CONTACT_NAME, KEY_USER_PHONE_NUM},
                null
                , null, null, null, null, null);
        if(null != cursors && cursors.moveToFirst()){
            do{
                songList.add(transContactCursor(cursors));
            }while(cursors.moveToNext());
        }
        return songList;
    }
    private ContactInfo transContactCursor(Cursor cursor){
        long id = cursor.getLong(cursor.getColumnIndex(KEY_CONTACT_ID));
        String name = cursor.getString(cursor.getColumnIndex(KEY_CONTACT_NAME));
        String number = cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE_NUM));
        return new ContactInfo(id,name,number);
    }
    private ContentValues transContactInfo(ContactInfo info) {
        ContentValues value = new ContentValues();
        value.put(KEY_CONTACT_ID, info.getContactId());
        value.put(KEY_CONTACT_NAME, info.getContactName());
        value.put(KEY_USER_PHONE_NUM, info.getContactNum());
        return value;
    }
}
