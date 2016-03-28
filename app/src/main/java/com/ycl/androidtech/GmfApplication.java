package com.ycl.androidtech;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.ycl.androidtech.database.DBManager;
import com.ycl.androidtech.hotfix.HotFixManager;
import com.ycl.androidtech.statistics.TimeMonitorConfig;
import com.ycl.androidtech.statistics.TimeMonitorManager;

/**
 * Created by yuchengluo on 2015/6/25.
 */
public class GmfApplication extends Application {

    private static Context mContext = null;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = this;
        TimeMonitorManager.getInstance().resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InitModule();
        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).recodingTimeTag("ApplicationCreate");
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public static Context getContext(){
        return mContext;
    }

    private void InitModule(){
        DBManager.InitDB(mContext);

    }
}
