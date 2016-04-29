package com.android.androidtech.utils;

import android.os.Build;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class Util4Phone {

    /**
     *
     * @Discription:TODO
     * @return
     */
    public static boolean isSupportAnimation() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}

