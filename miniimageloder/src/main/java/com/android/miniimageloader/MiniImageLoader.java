package com.android.miniimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.miniimageloader.cache.ImageCache;
import com.android.miniimageloader.config.BitmapConfig;
import com.android.miniimageloader.utils.BitmapUtil;
import com.android.miniimageloader.utils.CloseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yuchengluo on 2016/4/29.
 */
public class MiniImageLoader extends ImageLoader{
    private volatile static MiniImageLoader miniImageLoader = null;
    private ImageCache mImageCache = null;
    static private Context mContext = null;
    public static MiniImageLoader getInstance(){
        if(null == miniImageLoader){
            synchronized (MiniImageLoader.class){
                miniImageLoader = new MiniImageLoader();
            }
        }
        return miniImageLoader;
    }
    public static void progrem(Context context){
        mContext = context;
    }
    public MiniImageLoader(){
        mImageCache = new ImageCache(mContext);
    }
    @Override
    public Bitmap downLoadBitmap(String urlString,BitmapConfig bmConfig) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            getmImageCache().saveToDisk(urlString,in);
            final BitmapFactory.Options options = bmConfig.getBitmapOptions(in);
            in.close();
            urlConnection.disconnect();
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromStream(in, options,getmImageCache());
            return bitmap;

        } catch (final IOException e) {
            Log.e("text", "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseUtil.closeQuietly(in);
        }
        return null;
    }

    @Override
    protected ImageCache getmImageCache() {
        if(null == mImageCache){
            mImageCache = new ImageCache(mContext);
        }
        return mImageCache;
    }
}
