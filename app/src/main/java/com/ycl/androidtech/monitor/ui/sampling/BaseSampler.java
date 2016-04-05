package com.ycl.androidtech.monitor.ui.sampling;

import android.os.Handler;
import android.os.HandlerThread;

import com.ycl.androidtech.utils.GLog;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yuchengluo on 2016/4/1.
 * 采样基类，启动一个采样线程，具体采样数据由对应的子类完成
 */
public abstract class BaseSampler {
    private final String TAG = "BaseSampler";
    private Handler mControlHandler = null;
    private int intervalTime = 50; //ms采样间隔
    private AtomicBoolean mIsSampling = new AtomicBoolean(false);
    public BaseSampler(){
        GLog.d(TAG,"Init BaseSampler");
    }
    public void start(){
        if(!mIsSampling.get()) {
            GLog.d(TAG,"start Sampler");
            getmControlHandler().removeCallbacks(mRunnable);
            getmControlHandler().post(mRunnable);
            mIsSampling.set(true);
        }
    }
    public void stop(){
        if(mIsSampling.get()){
            GLog.d(TAG,"stop Sampler");
            getmControlHandler().removeCallbacks(mRunnable);
            mIsSampling.set(false);
        }
    }
    //创建一个HandlerThread,定时采样
    private Handler getmControlHandler(){
        if(null == mControlHandler){
            HandlerThread mHT = new HandlerThread("SamplerThread");
            mHT.start();
            mControlHandler = new Handler(mHT.getLooper());
        }
        return mControlHandler;
    }
    abstract void doSample();//采样抽象方法
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doSample();
            if(mIsSampling.get()){
                getmControlHandler().postDelayed(mRunnable,intervalTime);
            }
        }
    };
}
