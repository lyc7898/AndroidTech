package com.android.androidtech.monitor.ui.sampling;

import android.util.Log;

import com.android.androidtech.utils.GLog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by yuchengluo on 2016/4/1.
 */
public class CpuInfoSampler extends BaseSampler {
    private final String TAG = "CpuInfoSampler";
    private int mPid = -1;
    private ArrayList<CpuInfo> mCpuInfoList = new ArrayList<CpuInfo>();
    public CpuInfoSampler() {

    }
    @Override
    void doSample() {
        GLog.d(TAG, "doSample");
        dumpCpuInfo();
    }
    @Override
    public void start() {
        super.start();
        mUserPre = 0;
        mSystemPre = 0;
        mIdlePre = 0;
        mIoWaitPre = 0;
        mTotalPre = 0;
        mAppCpuTimePre = 0;
    }
    public void clearCache(){
        mCpuInfoList.clear();
    }
    public ArrayList<CpuInfo> getStatCpuInfoList(){
        return mCpuInfoList;
    }
    private void dumpCpuInfo() {
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1024);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                cpuRate = "";
            }
            if (mPid < 0) {
                mPid = android.os.Process.myPid();
            }
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + mPid + "/stat")), 1024);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                pidCpuRate = "";
            }
            parseCpuRate(cpuRate, pidCpuRate);
        } catch (Throwable ex) {
            Log.e(TAG, "doSample: ", ex);
        } finally {
            try {
                if (cpuReader != null) {
                    cpuReader.close();
                }
                if (pidReader != null) {
                    pidReader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "doSample: ", e);
            }
        }
    }
    private void parseCpuRate(String cpuRate, String pidCpuRate) {
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            return;
        }
        long user_time = Long.parseLong(cpuInfoArray[2]);
        long nice_time = Long.parseLong(cpuInfoArray[3]);
        long system_time = Long.parseLong(cpuInfoArray[4]);
        long idle_time = Long.parseLong(cpuInfoArray[5]);
        long ioWait_time = Long.parseLong(cpuInfoArray[6]);
        long total_time = user_time + nice_time + system_time + idle_time + ioWait_time + Long.parseLong(cpuInfoArray[7]) + Long.parseLong(cpuInfoArray[8]);
        String[] pidCpuInfos = pidCpuRate.split(" ");
        if (pidCpuInfos.length < 17) {
            return;
        }
        long appCpu_time = Long.parseLong(pidCpuInfos[13]) + Long.parseLong(pidCpuInfos[14])
                + Long.parseLong(pidCpuInfos[15]) + Long.parseLong(pidCpuInfos[16]);
        if (mAppCpuTimePre > 0) {
            CpuInfo mCi = new CpuInfo(System.currentTimeMillis());
            long idleTime = idle_time - mIdlePre;
            long totalTime = total_time - mTotalPre;
            mCi.mCpuRate = (totalTime - idleTime) * 100L / totalTime;
            mCi.mAppRate = (appCpu_time - mAppCpuTimePre) * 100L / totalTime;
            mCi.mSystemRate = (system_time - mSystemPre) * 100L / totalTime;
            mCi.mUserRate = (user_time - mUserPre) * 100L / totalTime;
            mCi.mIoWait = (ioWait_time - mIoWaitPre) * 100L / totalTime;
            synchronized (mCpuInfoList) {
                mCpuInfoList.add(mCi);
                GLog.d(TAG,"cpu info :" + mCi.toString());
            }
        }
        mUserPre = user_time;
        mSystemPre = system_time;
        mIdlePre = idle_time;
        mIoWaitPre = ioWait_time;
        mTotalPre = total_time;
        mAppCpuTimePre = appCpu_time;
    }
    private long mUserPre = 0;
    private long mSystemPre = 0;
    private long mIdlePre = 0;
    private long mIoWaitPre = 0;
    private long mTotalPre = 0;
    private long mAppCpuTimePre = 0;
}
