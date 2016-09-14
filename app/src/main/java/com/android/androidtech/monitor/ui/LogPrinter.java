package com.android.androidtech.monitor.ui;

import android.util.Printer;

import com.android.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2016/3/31.
 * hook loop
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
            //发送消息，同时启动线程保存状态
            startTime = System.currentTimeMillis();
            mLogPrinter.onStartLoop();
        } else {
            //执行消息，同时复位ANR线程状态
            long endtime = System.currentTimeMillis();

            execuTime(x, startTime,endtime);
            startTime = 0;
        }
    }

    private void execuTime(String loginfo, long starttime,long endtime) {
        int level = 0;
        long time = endtime - starttime;
        GLog.d(TAG, "dispatch handler time : " + time);
        if (time > TIME_WARNING_LEVEL_2) {
            GLog.e(TAG, "Warning_LEVEL_2:\r\n" + "println:" + loginfo);
            level = UI_PERF_LEVEL_2;
        } else if (time > TIME_WARNING_LEVEL_1) {
            GLog.d(TAG, "Warning_LEVEL_1:\r\n" + "println:" + loginfo);
            level = UI_PERF_LEVEL_1;
        }
        mLogPrinter.onEndLoop(starttime,endtime,loginfo, level);
    }
}
