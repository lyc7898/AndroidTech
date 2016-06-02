package com.android.androidtech;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yuchengluo on 2016/6/1.
 */
public class GmfSharedPreferences {
    private final String SHAREPREFERENCES_NAME = "GmfSharedPreferences";
    private final String KEY_SYNC_SYSTEM_CONTACT_STATE = "KEY_SYNC_SYSTEM_CONTACT_STATE";




    private static GmfSharedPreferences mInstance = null;
    private static Context mContext = null;
    protected  SharedPreferences mPreferences;
    public static void progrem(Context ctx){
        mContext = ctx;
    }
    public static GmfSharedPreferences getInstance(){
        if(null == mInstance){
            mInstance = new GmfSharedPreferences();
        }
        return mInstance;
    }
    public GmfSharedPreferences(){
        if (mPreferences == null) {
            if (mContext != null) {
                mPreferences = mContext.getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
            }
        }
    }

    public void setSyncSysContactState(boolean enable) {
        if (mPreferences != null) {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(KEY_SYNC_SYSTEM_CONTACT_STATE, enable);
            editor.commit();
        }
    }

    public boolean getSyncSysContactState() {
        if (mPreferences != null) {
            return mPreferences.getBoolean(KEY_SYNC_SYSTEM_CONTACT_STATE, false);
        }
        return true;
    }
}
