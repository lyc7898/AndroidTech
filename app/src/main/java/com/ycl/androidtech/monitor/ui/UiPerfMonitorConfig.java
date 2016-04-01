package com.ycl.androidtech.monitor.ui;

import android.os.Environment;

/**
 * Created by yuchengluo on 2016/3/31.
 */
public interface UiPerfMonitorConfig {

    //告警域值的设定
    public int TIME_WARNING_LEVEL_1 = 100;   //定义卡顿靠警域值
    public int TIME_WARNING_LEVEL_2 = 300;//需要上报线程信息域值
    //卡顿级别 0 无卡顿
    public int UI_PERF_LEVEL_0 = 0;
    public int UI_PERF_LEVEL_1 = 1;
    public int UI_PERF_LEVEL_2 = 2;
    //监控器状态
    public int UI_PERF_MONITER_START = 0x01;
    public int UI_PERF_MONITER_STOP = 0x01 << 1;

    public String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + "androidtech/uiperf";
}
