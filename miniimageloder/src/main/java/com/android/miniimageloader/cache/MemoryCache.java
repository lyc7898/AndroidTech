package com.android.miniimageloader.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

import com.android.miniimageloader.utils.MLog;

/**
 * Created by yuchengluo on 2016/5/12.
 */
public class MemoryCache{
    private final int DEFAULT_MEM_CACHE_SIZE = 1024 * 12;
    private LruCache<String,Bitmap> mMemoryCache;
    private final String TAG = "MemoryCache";
    public MemoryCache(float sizePer){
        Init(sizePer);
    }
    private void Init(float sizePer){
        int cacheSize = DEFAULT_MEM_CACHE_SIZE;
        if(sizePer> 0){
            cacheSize = Math.round(sizePer * Runtime.getRuntime().maxMemory() / 1024);
        }
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return  bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(url);
        }
        if (bitmap != null) {
            MLog.d(TAG, "Memory cache EXIET!");
        }
        return bitmap;
    }
    public void addBitmapToCache(String url,Bitmap bitmap) {
        if (url == null || bitmap == null) {
            return;
        }
        MLog.d(TAG,"addBitmapToCache size:" + getBitmapSize(bitmap));
        mMemoryCache.put(url, bitmap);
    }
    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }
}
