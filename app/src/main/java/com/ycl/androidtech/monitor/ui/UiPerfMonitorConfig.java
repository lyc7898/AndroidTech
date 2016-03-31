package com.ycl.androidtech.monitor.ui;

import android.os.Environment;

/**
 * Created by yuchengluo on 2016/3/31.
 */
public interface UiPerfMonitorConfig {
    public int WARNING_TIME = 100;   //定义卡顿靠警域值
    public int DUMP_THREAD_INFO_TIME = 300;//需要上报线程信息域值
    public String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "androidtech/uiperf";
}
