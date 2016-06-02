package com.android.androidtech.business.contact;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by yuchengluo on 2016/3/25.
 */
public class ContactsManager {
    private static ContactsManager mContactsManager = null;
    private static Context mContext  = null;
    private ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
    public static void programStart(Context ctx){
        mContext = ctx;
    }
    public static synchronized ContactsManager getInstance(){
        if(null == mContactsManager){
            mContactsManager = new ContactsManager();
        }
        return mContactsManager;
    }
    public ArrayList<ContactInfo> getTestContactInfo(){
        for (int i = 1; i <= 40; i++) {
            ContactInfo info = new ContactInfo(i,"姓名" + i,"num" + (139 + i));
            contactInfos.add(info);
        }
        return contactInfos;
    }
}
