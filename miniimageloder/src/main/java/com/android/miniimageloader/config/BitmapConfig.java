package com.android.miniimageloader.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by yuchengluo on 2016/5/9.
 */
public class BitmapConfig {
    private int mWidth, mHeight;
    private Bitmap.Config mPreferred;

    public BitmapConfig(int width, int height) {
        this(width, height, Bitmap.Config.RGB_565);
    }

    public BitmapConfig(int width, int height, Bitmap.Config preferred) {
        this.mWidth = width;
        this.mHeight = height;
        this.mPreferred = preferred;
    }

    public BitmapFactory.Options getBitmapOptions() {
        return getBitmapOptions(null);
    }

    //精确计算，需要图片is流先解码,再计算宽高比
    public BitmapFactory.Options getBitmapOptions(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if(is != null) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            options.inSampleSize = calculateInSampleSize(options, mWidth, mHeight);
        }
        options.inJustDecodeBounds = false;
        return options;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
