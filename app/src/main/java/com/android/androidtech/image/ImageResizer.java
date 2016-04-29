package com.android.androidtech.image;

import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION_CODES;

public class ImageResizer extends ImageWorker{
	
	private static final String TAG = "ImageResizer";
	private int mImageWidth;
	private int mImageHeight;

	public ImageResizer(Context context, int imageWidth, int imageHeight) {
        super(context);
        setImageSize(imageWidth, imageHeight);
    }
	
	public ImageResizer(Context context, int imageSize) {
        super(context);
        setImageSize(imageSize, imageSize);
    }
	
	protected ImageResizer(Context context) {
		super(context);
	}
	
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}

	public int getmImageWidth() {
		return mImageWidth;
	}

	public int getmImageHeight() {
		return mImageHeight;
	}

	@Override
	protected Bitmap processBitmap(String url) {
		return null;
	}
	
	public static BitmapFactory.Options getSampledBitmapOptionsFromStream(
			InputStream is, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;

		return options;
	}

	public static Bitmap decodeSampledBitmapFromStream(
			InputStream is, BitmapFactory.Options options, ImageCache cache) {

		if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
            addInBitmapOptions(options, cache);
        }

		return BitmapFactory.decodeStream(is, null, options);
	}

	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private static void addInBitmapOptions(BitmapFactory.Options options, ImageCache cache) {

	    	options.inMutable = true;
	    	if (cache != null) {
	    		Bitmap inBitmap = cache.getBitmapFromReusableSet(options);

	    		if (inBitmap != null) {
	                options.inBitmap = inBitmap;
	            }
	    	}
	    }

    /**
    * @param @param options
    * @param @param reqWidth
    * @param @param reqHeight
    * @param @return
    * @throws
    */
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
