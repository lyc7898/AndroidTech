package com.android.miniimageloader.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

import com.android.miniimageloader.config.BitmapConfig;
import com.android.miniimageloader.config.MiniImageLoaderConfig;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yuchengluo on 2016/4/29.
 */
public class ImageCache {
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;
    public ImageCache(Context ctx){
        mMemoryCache = new MemoryCache(0.4f);
        mDiskCache = new DiskCache(ctx, MiniImageLoaderConfig.DEFAULT_DISK_CACHE_SIZE);
    }
    public Bitmap getBitmap(String url){
        Bitmap bitmap = mMemoryCache.getBitmap(url);

        return bitmap;
    }
    public void addToCache(String url,Bitmap bitmap){
        mMemoryCache.addBitmapToCache(url,bitmap);
    }
    public Bitmap getBitmapFromDisk(String url,BitmapConfig config){
        return mDiskCache.getBitmapFromDiskCache(url,config);
    }
    public void saveToDisk(String url,InputStream is){
        mDiskCache.saveToDisk(url,is);
    }
    public Bitmap getBitmapFromReusableSet(BitmapFactory.Options options){
        return mMemoryCache.getBitmapFromReusableSet(options);
    }
}