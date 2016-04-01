package com.ycl.androidtech.monitor.ui;

import android.os.Environment;

/**
 * Created by yuchengluo on 2016/3/31.
 */
public interface UiPerfMonitorConfig {
    public int TIME_WARNING_LEVEL_1 = 100;   //定义卡顿靠警域值
    public int TIME_WARNING_LEVEL_2 = 300;//需要上报线程信息域值
    public String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "androidtech/uiperf";
}
