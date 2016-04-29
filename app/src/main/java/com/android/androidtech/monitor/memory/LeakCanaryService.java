package com.android.androidtech.monitor.memory;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;
import com.android.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2016/4/20.
 */
public class LeakCanaryService extends DisplayLeakService{
    private final String TAG = "LeakCanaryService";
    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        GLog.d(TAG,"afterDefaultHandling:" + leakInfo);
        //super.afterDefaultHandling(heapDump, result, leakInfo);
    }
}
