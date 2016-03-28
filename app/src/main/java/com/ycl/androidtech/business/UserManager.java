package com.ycl.androidtech.business;

import android.content.Context;

/**
 * Created by yuchengluo on 2016/3/25.
 */
public class UserManager {
    private static UserManager mUseManager = null;
    private static Context mContext  = null;

    public static void programStart(Context ctx){
        mContext = ctx;
    }
}
