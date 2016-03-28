package com.ycl.androidtech.statistics;

import com.ycl.androidtech.utils.GLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by yuchengluo on 2016/3/25.
 * 描述一个耗时统计的对象
 */
public class TimeMonitor {
    private final String TAG = "TimeMonitor";
    private int monitorId = -1;
    //保存一个耗时统计模块的各种耗时，tag对应某一个阶段的时间
    private LinkedHashMap<String, Long> mTimeTag = new LinkedHashMap<String, Long>();
    private long mStartTime = 0;

    public TimeMonitor(int id) {
        GLog.d(TAG,"init TimeMonitor id:" + id);
        monitorId = id;
        startMoniter();
    }

    public int getMonitorId() {
        return monitorId;
    }

    public void startMoniter() {
        //每次重新启动，都需要把前面的数据清除，避免统计到错误的数据
        if (mTimeTag.size() > 0) {
            mTimeTag.clear();
        }
        mStartTime = System.currentTimeMillis();
    }

    //打一次点，tag交线需要统计的上层自定义
    public void recodingTimeTag(String tag) {
        //检查是否保存过相同的TAG
        if (mTimeTag.get(tag) != null) {
            mTimeTag.remove(tag);
        }
        long time = System.currentTimeMillis() - mStartTime;
        //GLog.d(TAG, tag + ":" + time + "ms");
        mTimeTag.put(tag, time);
    }
    public void end(String tag,boolean writeLog){
        recodingTimeTag(tag);
        end(writeLog);
    }
    public void end(boolean writeLog) {
        if (writeLog) {
            //写入到本地文件
        }
        testShowData();
    }
    public void testShowData(){
        if(mTimeTag.size() <= 0){
            GLog.e(TAG,"mTimeTag is empty!");
            return;
        }
        Iterator iterator = mTimeTag.keySet().iterator();
        while (iterator != null && iterator.hasNext()){
           String tag = (String)iterator.next();
            GLog.d(TAG,tag + ":" +  mTimeTag.get(tag));
        }
    }
    public HashMap<String, Long> getTimeTags() {
        return mTimeTag;
    }
}
