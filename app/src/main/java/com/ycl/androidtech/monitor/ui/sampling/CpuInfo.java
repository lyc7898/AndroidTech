package com.ycl.androidtech.monitor.ui.sampling;

/**
 * Created by yuchengluo on 2016/4/5.
 * CPU信息数据结构
 */
public class CpuInfo {
    public long mId = 0;//一个CPU信息的ID
    public long mCpuRate = 0;//总的CPU使用率
    public long mAppRate = 0;//当前app CPU使用率
    public long mUserRate = 0;//用户进程
    public long mSystemRate = 0;//系统进程
    public long mIoWait = 0;//等待时间
    public CpuInfo(long id) {
        mId = id;
    }
    public String toString() {
        StringBuffer mci = new StringBuffer();
        mci.append("cpu:").append(mCpuRate).append("% ");
        mci.append("app:").append(mAppRate).append("% ");
        mci.append("[").append("user:").append(mUserRate).append("% ");
        mci.append("system:").append(mSystemRate).append("% ");
        mci.append("ioWait:").append(mIoWait).append("% ]");
        return mci.toString();
    }
}
