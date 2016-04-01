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
            mLogPrinter.onStartLoop();
        } else {
            long time = System.currentTimeMillis() - startTime;
            GLog.d(TAG, "dispatch handler time : " + time);
            execuTime(x, time);
            startTime = 0;
        }
    }

    //根据需要可以定义更多级别
    private void execuTime(String loginfo, long time) {
        int level = 0;
        if (time > TIME_WARNING_LEVEL_2) {
            GLog.e(TAG, "Warning_LEVEL_2:\r\n" + "println:" + loginfo);
            level = UI_PERF_LEVEL_2;
        } else if (time > TIME_WARNING_LEVEL_1) {
            GLog.d(TAG, "Warning_LEVEL_1:\r\n" + "println:" + loginfo);
            level = UI_PERF_LEVEL_1;
        }
        mLogPrinter.onEndLoop(loginfo, level);
    }
}
