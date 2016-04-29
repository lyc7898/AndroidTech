package com.android.androidtech.image;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

public class ImageFetcher extends ImageResizer {

	public ImageFetcher(Context context) {
		super(context);
	}
	
	public ImageFetcher(Context context, int imageSize) {
		super(context, imageSize);
	}

	@Override
	protected Bitmap processBitmap(String url) {
		
		return downLoadByUrl(url);
	}
	
    public Bitmap downLoadByUrl(String urlString) {
        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            final BitmapFactory.Options options =
            		getSampledBitmapOptionsFromStream(in, getmImageWidth(), getmImageHeight());
            in.close();
            urlConnection.disconnect();
            //TODO  CHECK
            urlConnection = (HttpURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            
            Bitmap bitmap = decodeSampledBitmapFromStream(in, options, getImageCache());
            
            return bitmap;
            
        } catch (final IOException e) {
            Log.e("text", "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {}
        }
		return null;
    }
	
    public static void disableConnectionReuseIfNecessary() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
