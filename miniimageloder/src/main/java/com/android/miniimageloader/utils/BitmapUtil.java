package com.android.miniimageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by yuchengluo on 2016/5/12.
 */
public class BitmapUtil {
    static public Bitmap decodeSampledBitmapFromStream(
            InputStream is, BitmapFactory.Options options) {
        MLog.d("BitmapUtil", "mSampleSize:" + options.inSampleSize);
        return BitmapFactory.decodeStream(is, null, options);
    }
}
