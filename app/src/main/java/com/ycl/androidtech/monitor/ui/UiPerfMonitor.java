package com.ycl.androidtech.monitor.ui;

import android.os.Looper;

import com.ycl.androidtech.monitor.ui.sampling.CpuInfoSampler;

import java.io.File;

/**
 * Created by yuchengluo on 2016/3/31.
 * UI卡顿监控管理类
 */
public class UiPerfMonitor implements UiPerfMonitorConfig,LogPrinterListener{
    private static UiPerfMonitor mInstance = null;
    private LogPrinter mLogPrinter;
    private int monitorState = UI_PERF_MONITER_STOP;
    private CpuInfoSampler mCpuInfoSampler = null;
    public synchronized static UiPerfMonitor getmInstance(){
        if(null == mInstance){
            mInstance = new UiPerfMonitor();
        }
        return mInstance;
    }

    //初始化
    public UiPerfMonitor(){
        mCpuInfoSampler = new CpuInfoSampler();
        mLogPrinter = new LogPrinter(this);
        initLogpath();
    }

    public void startMonitor(){
        Looper.getMainLooper().setMessageLogging(mLogPrinter);
        monitorState = UI_PERF_MONITER_START;
    }
    public void stopMonitor(){
        Looper.getMainLooper().setMessageLogging(null);
        mCpuInfoSampler.stop();
        monitorState = UI_PERF_MONITER_STOP;
    }
    public boolean isMonitoring(){
        return monitorState == UI_PERF_MONITER_START;
    }
    //初始化日志路径
    private void initLogpath(){
        File logpath = new File(LOG_PATH);
        if(!logpath.exists()){
            logpath.mkdir();
        }
    }


    @Override
    public void onStartLoop() {
        mCpuInfoSampler.start();
    }

    @Override
    public void  onEndLoop(long starttime,long endtime,String loginfo,int level) {
        mCpuInfoSampler.stop();
    }

}
