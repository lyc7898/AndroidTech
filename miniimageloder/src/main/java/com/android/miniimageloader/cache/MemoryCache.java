package com.android.miniimageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by yuchengluo on 2016/5/12.
 */
public class MemoryCache {
    private LruCache<String,Bitmap> mMemoryCache;

    public MemoryCache(float sizePer){
        Init(sizePer);
    }
    private void Init(float sizePer){
        if(null == mMemoryCache){

        }
    }
}
