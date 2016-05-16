package com.android.miniimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.miniimageloader.config.BitmapConfig;
import com.android.miniimageloader.config.MiniImageLoaderConfig;
import com.android.miniimageloader.utils.BitmapUtil;
import com.android.miniimageloader.utils.FileUtil;
import com.android.miniimageloader.utils.MLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yuchengluo on 2016/5/13.
 */
public class DiskCache {
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private final String DISK_CACHE_SUBDIR = "thumbnails";
    private final String TAG = "DiskCache";
    private boolean mDiskCacheStarting = true;
    public DiskCache(Context ctx,long cacheSize) {
        init(cacheSize, getDiskCacheDir(ctx, DISK_CACHE_SUBDIR));
    }

    private void init(final long cacheSize,final File cacheFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mDiskCacheLock) {
                    if(!cacheFile.exists()){
                        cacheFile.mkdir();
                    }
                    MLog.d(TAG,"Init DiskLruCache cache path:" + cacheFile.getPath() + "\r\n" + "Disk Size:" + cacheSize);
                    try {
                        mDiskLruCache = DiskLruCache.open(cacheFile, MiniImageLoaderConfig.VESION_IMAGELOADER, 1, cacheSize);
                        mDiskCacheStarting = false; // Finished initialization
                        mDiskCacheLock.notifyAll(); // Wake any waiting threads
                    }catch(IOException e){
                        MLog.e(TAG,"Init err:" + e.getMessage());
                    }
                }
            }
        }).start();
    }
    public void saveToDisk(String imageUrl, InputStream in) {
        // add to disk cache
        synchronized (mDiskCacheLock) {
            try {
                while (mDiskCacheStarting) {
                    try {
                        mDiskCacheLock.wait();
                    } catch (InterruptedException e) {}
                }
                String key = hashKeyForDisk(imageUrl);
                MLog.d(TAG,"saveToDisk get key:" + key);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (in != null && editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    MLog.d(TAG, "saveToDisk");
                    if (FileUtil.copyStream(in,outputStream)) {
                        MLog.d(TAG, "saveToDisk commit start");
                        editor.commit();
                        MLog.d(TAG, "saveToDisk commit over");
                    } else {
                        editor.abort();
                        MLog.e(TAG, "saveToDisk commit abort");
                    }
                }
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Bitmap  getBitmapFromDiskCache(String imageUrl,BitmapConfig bitmapconfig) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {}
            }
            if (mDiskLruCache != null) {
                try {

                    String key = hashKeyForDisk(imageUrl);
                    MLog.d(TAG,"getBitmapFromDiskCache get key:" + key);
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                    if(null == snapShot){
                        return null;
                    }
                    InputStream is = snapShot.getInputStream(0);
                    if(is != null){
                        final BitmapFactory.Options options = bitmapconfig.getBitmapOptions();
                        return BitmapUtil.decodeSampledBitmapFromStream(is, options);
                    }else{
                        MLog.e(TAG,"is not exist");
                    }
                }catch (IOException e){
                    MLog.e(TAG,"getBitmapFromDiskCache ERROR");
                }
            }
        }
        return null;
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    public File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
}
