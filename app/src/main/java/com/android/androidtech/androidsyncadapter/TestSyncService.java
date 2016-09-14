package com.android.androidtech.androidsyncadapter;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by yuchengluo on 2016/7/14.
 */

public class TestSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static TestSyncAdapter mSyncAdapter = null;
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new TestSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }

    static class TestSyncAdapter extends AbstractThreadedSyncAdapter {
        public TestSyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }
        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            //具体的同步操作，这里主要是为了提高进程优先级
            //getContext().getContentResolver().notifyChange(XXAccountProvider.CONTENT_URI, null, false);
        }
    }
}