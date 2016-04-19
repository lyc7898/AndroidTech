package com.ycl.androidtech.monitor.ui;

import android.os.Environment;

/**
 * Created by yuchengluo on 2016/3/31.
 */
public interface UiPerfMonitorConfig {

    //time level
    public final int TIME_WARNING_LEVEL_1 = 5;   //
    public final int TIME_WARNING_LEVEL_2 = 300;//
    //
    public final int UI_PERF_LEVEL_0 = 0;
    public final int UI_PERF_LEVEL_1 = 1;
    public final int UI_PERF_LEVEL_2 = 2;
    //
    public final int UI_PERF_MONITER_START = 0x01;
    public final int UI_PERF_MONITER_STOP = 0x01 << 1;

    public final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "/androidtech";
    public final String FILENAME = "UiMonitorLog";
}
