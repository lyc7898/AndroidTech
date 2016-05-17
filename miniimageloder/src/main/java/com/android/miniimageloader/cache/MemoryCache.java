package com.android.miniimageloader.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

import com.android.miniimageloader.utils.MLog;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yuchengluo on 2016/5/12.
 */
public class MemoryCache{
    private final int DEFAULT_MEM_CACHE_SIZE = 1024 * 12;
    private LruCache<String,Bitmap> mMemoryCache;
    private Set<SoftReference<Bitmap>> mReusableBitmaps;
    private final String TAG = "MemoryCache";

    public MemoryCache(float sizePer){
        Init(sizePer);
    }
    private void Init(float sizePer){
        int cacheSize = DEFAULT_MEM_CACHE_SIZE;
        if(sizePer> 0){
            cacheSize = Math.round(sizePer * Runtime.getRuntime().maxMemory() / 1024);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mReusableBitmaps =
                    Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue));
                }
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
        }
        int width = targetOptions.outWidth / targetOptions.inSampleSize;
        int height = targetOptions.outHeight / targetOptions.inSampleSize;

        int byteCount = width * height * getBytesPerPixel(candidate.getConfig());

        return byteCount <= candidate.getAllocationByteCount();
    }
    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }
    //获取inBitmap,实现内存复用
    public Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
        Bitmap bitmap = null;

        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
            Bitmap item;

            while (iterator.hasNext()) {
                item = iterator.next().get();

                if (null != item && item.isMutable()) {
                    if (canUseForInBitmap(item, options)) {

                        Log.v("TEST", "canUseForInBitmap!!!!");

                        bitmap = item;

                        // Remove from reusable set so it can't be used again
                        iterator.remove();
                        break;
                    }
                } else {
                    // Remove from the set if the reference has been cleared.
                    iterator.remove();
                }
            }
        }

        return bitmap;
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
