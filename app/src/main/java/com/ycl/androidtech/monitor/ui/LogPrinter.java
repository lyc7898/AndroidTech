package com.ycl.androidtech.monitor.ui;

import android.util.Printer;

import com.ycl.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2016/3/31.
 * Í¨¹ýLoop
 */
public class LogPrinter implements Printer{
    private final String TAG = "LogPrinter";
    private int print_num = 1;
    @Override
    public void println(String x) {
        GLog.d(TAG,"println:" + x +"\r\n num: " + (print_num++));
    }
}
