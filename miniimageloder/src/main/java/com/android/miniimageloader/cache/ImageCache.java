package com.android.miniimageloader.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;

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
    public ImageCache(){
        mMemoryCache = new MemoryCache(0.4f);
    }
    public Bitmap getBitmap(String url){
        Bitmap bitmap = mMemoryCache.getBitmap(url);

        return bitmap;
    }
    public void addToCache(String url,Bitmap bitmap){
        mMemoryCache.addBitmapToCache(url,bitmap);
    }
    public String getDiskUrl(String url){
        return null;
    }
    public void saveToDisk(String url,InputStream is){

    }
}