package com.ycl.androidtech.utils;

import android.os.Build;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class Util4Phone {

    /**
     * 判断是否支持动画，目前的判断条件是系统版本 >= 3.0，后续可以添加更多条件
     *
     * @Discription:TODO
     * @return
     */
    public static boolean isSupportAnimation() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}

