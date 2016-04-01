package com.ycl.androidtech.monitor.ui.sampling;

import com.ycl.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2016/4/1.
 * 采样CPU数据
 */
public class CpuInfoSampler extends BaseSampler{
    private final String TAG = "CpuInfoSampler";
    public CpuInfoSampler(){

    }
    @Override
    void doSample() {
        GLog.d(TAG,"doSample");
    }
}
