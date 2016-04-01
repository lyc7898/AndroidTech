package com.ycl.androidtech.monitor.ui;

import android.util.Printer;

import com.ycl.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2016/3/31.
 * 通过Loop
 */
public class LogPrinter implements Printer, UiPerfMonitorConfig {
    private final String TAG = "LogPrinter";
    private LogPrinterListener mLogPrinter = null;
    private long startTime = 0;

    public LogPrinter(LogPrinterListener listener) {
        mLogPrinter = listener;
    }
    @Override
    public void println(String x) {
        if (startTime <= 0) {
            startTime = System.currentTimeMillis();
        } else {
            long time = System.currentTimeMillis() - startTime;
            GLog.d(TAG, "dispatch handler time : " + time);
            if (time > TIME_WARNING_LEVEL_1) {
                GLog.d(TAG, "Warning:\r\n" + "println:" + x);
                execuTime(x,time);
            }
            startTime = 0;
        }
    }
    //根据需要可以定义更多级别
    private void execuTime(String loginfo,long time) {
        if (time > TIME_WARNING_LEVEL_2) {
            mLogPrinter.onWaringLevel2(loginfo,time);
        } else {
            mLogPrinter.onWaringLevel1(loginfo, time);
        }
    }
}
