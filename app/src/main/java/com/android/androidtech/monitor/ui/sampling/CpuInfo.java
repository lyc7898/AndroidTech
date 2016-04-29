package com.android.androidtech.monitor.ui.sampling;

/**
 * Created by yuchengluo on 2016/4/5.
 */
public class CpuInfo {
    public long mId = 0;
    public long mCpuRate = 0;
    public long mAppRate = 0;
    public long mUserRate = 0;
    public long mSystemRate = 0;
    public long mIoWait = 0;
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
