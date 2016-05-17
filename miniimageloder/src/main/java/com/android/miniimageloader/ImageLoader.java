package com.android.miniimageloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.widget.ImageView;

import com.android.miniimageloader.cache.ImageCache;
import com.android.miniimageloader.config.BitmapConfig;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by yuchengluo on 2016/5/5.
 */
public abstract class ImageLoader {
    private boolean mExitTasksEarly = false;  // 是否提前退出的标志
    protected boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    public final String TAG = "ImageLoader";

    protected ImageLoader() {
        Init();
    }

    private void Init() {
    }

    public void loadImage(String url, ImageView imageView, BitmapConfig bmConfig) {
        if (url == null) {
            return;
        }
        Bitmap bitmap =  getmImageCache().getBitmap(url);;
        if (bitmap != null) {
            setImageBitmap(imageView,bitmap);
        }
        //否则走加载
        else {

            final BitmapLoadTask task = new BitmapLoadTask(url, imageView, bmConfig);

            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR);
        }
    }

    private class BitmapLoadTask extends
            AsyncTask<Void, Void, Bitmap> {
        private String mUrl;
        private BitmapConfig mBitmapConfig = null;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapLoadTask(String url, ImageView imageView, BitmapConfig bmConfig) {
            mUrl = url;
            imageViewReference = new WeakReference<ImageView>(imageView);
            mBitmapConfig = bmConfig;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (bitmap == null && !isCancelled()
                    && imageViewReference.get() != null && !mExitTasksEarly) {
                bitmap = getmImageCache().getBitmapFromDisk(mUrl, mBitmapConfig);
            }

            if (bitmap == null && !isCancelled()
                    && imageViewReference.get() != null && !mExitTasksEarly) {
                bitmap = downLoadBitmap(mUrl, mBitmapConfig);
            }
            if (bitmap != null) {
                getmImageCache().addToCache(mUrl, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled() || mExitTasksEarly) {
                result = null;
            }

            ImageView imageView = imageViewReference.get();
            if (result != null && imageView != null) {
                setImageBitmap(imageView, result);
            }
        }

        @Override
        protected void onCancelled(Bitmap value) {
            super.onCancelled(value);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    /**
     * @param imageView
     * @param drawable
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
        setPauseWork(false);
    }

    /**
     * 下载
     *
     * @param url
     * @return
     */
    protected abstract Bitmap downLoadBitmap(String url, BitmapConfig bmConfig);

    protected abstract ImageCache getmImageCache();
}
