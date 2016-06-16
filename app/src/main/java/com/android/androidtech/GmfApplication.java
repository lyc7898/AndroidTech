package com.android.androidtech;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.android.androidtech.business.contact.ContactsManager;
import com.android.miniimageloader.MiniImageLoader;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.android.androidtech.database.DBManager;
import com.android.androidtech.monitor.memory.LeakCanaryService;
import com.android.androidtech.monitor.time.TimeMonitorConfig;
import com.android.androidtech.monitor.time.TimeMonitorManager;
/**
 * Created by yuchengluo on 2015/6/25.
 */
public class GmfApplication extends Application {

    private static Context mContext = null;
    private static RefWatcher mRefWatcher = null;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = this;
        TimeMonitorManager.getInstance().resetTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START);
        //初始化图片引擎
        MiniImageLoader.progrem(mContext);
        MiniImageLoader.getInstance();
        GmfSharedPreferences.progrem(mContext);
        ContactsManager.programStart(mContext);
    }
    public static  Context getmContext(){
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher  = LeakCanary.install(this, LeakCanaryService.class, AndroidExcludedRefs.createAppDefaults().build());

        InitModule();
        TimeMonitorManager.getInstance().getTimeMonitor(TimeMonitorConfig.TIME_MONITOR_ID_APPLICATION_START).recodingTimeTag("ApplicationCreate");
    }
    public static RefWatcher getRefWatcher(){
        return mRefWatcher;
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
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(this);
    }
}
