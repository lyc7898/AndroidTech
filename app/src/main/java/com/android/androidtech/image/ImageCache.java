package com.android.androidtech.image;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ImageCache {
	private static final String TAG = "TEST";

	//
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; //
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;

	//
	private LruCache<String, BitmapDrawable> mMemoryCache;

	private ImageCacheParams mCacheParams;

	private Set<SoftReference<Bitmap>> mReusableBitmaps;


	public static class ImageCacheParams {
		//mem
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;

		/***
		 *
		 */
		public void setMemCacheSizePercent(float percent) {
			if (percent < 0.01f || percent > 0.8f) {
				throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
						+ "between 0.01 and 0.8 (inclusive)");
			}
			memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
		}
	}

	protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
		Bitmap bitmap = null;

		if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
			final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
			Bitmap item;

			while (iterator.hasNext()) {
				item = iterator.next().get();

				if (null != item && item.isMutable()) {
					if (canUseForInBitmap(item, options)) {

						Log.v("TEST", "canUseForInBitmap!!!!");

                        bitmap = item;

                        // Remove from reusable set so it can't be used again
                        iterator.remove();
                        break;
                    }
				} else {
					// Remove from the set if the reference has been cleared.
					iterator.remove();
				}
			}
		}

		return bitmap;
	}

	@TargetApi(VERSION_CODES.KITKAT)
	private static boolean canUseForInBitmap(
			Bitmap candidate, BitmapFactory.Options targetOptions) {

		if (Build.VERSION.SDK_INT < VERSION_CODES.KITKAT) {
			return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
		}
		int width = targetOptions.outWidth / targetOptions.inSampleSize;
		int height = targetOptions.outHeight / targetOptions.inSampleSize;

		int byteCount = width * height * getBytesPerPixel(candidate.getConfig());

		return byteCount <= candidate.getAllocationByteCount();
	}

	/**
     * Return the byte usage per pixel of a bitmap based on its configuration.
     * @param config The bitmap configuration.
     * @return The byte usage per pixel.
     */
    private static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

	/**
	 *
	 * @param fragmentManager
	 * @param cacheParams
	 * @return
	 */
	public static ImageCache getInstance(
			FragmentManager fragmentManager, ImageCacheParams cacheParams) {
		final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);
		ImageCache imageCache = (ImageCache) mRetainFragment.getObject();
		if (imageCache == null) {
            imageCache = new ImageCache(cacheParams);
            mRetainFragment.setObject(imageCache);
        }

        return imageCache;
	}

    private ImageCache(ImageCacheParams cacheParams) {
        init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams) {
    	mCacheParams = cacheParams;
    	if (mCacheParams.memoryCacheEnabled) {
    		if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
    			mReusableBitmaps =
                        Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
    		}

    		mMemoryCache = new LruCache<String, BitmapDrawable>(mCacheParams.memCacheSize) {

				@Override
				protected void entryRemoved(boolean evicted, String key,
						BitmapDrawable oldValue, BitmapDrawable newValue) {
					if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
						//���ٻ������
						((RecyclingBitmapDrawable) oldValue).setIsCached(false);
					} else {
						//��������ü���
						if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
							mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
						}
					}
				}

				@Override
				protected int sizeOf(String key, BitmapDrawable bitmapDrawable) {
					final int bitmapSize = getBitmapSize(bitmapDrawable) / 1024;
					return bitmapSize == 0 ? 1 : bitmapSize;
				}

    		};
    	}
    }

    public void clearCache() {
    	if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }


    /**
     * @param bitmapDrawable
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
	public static int getBitmapSize(BitmapDrawable bitmapDrawable) {

    	Bitmap bitmap = bitmapDrawable.getBitmap();
    	//4.4
    	if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
    		return bitmap.getAllocationByteCount();
    	}
    	//3.1������
    	if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
    		return  bitmap.getByteCount();
    	}
    	//3.1֮ǰ�İ汾
    	return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * @param url
     * @return
     */
    public BitmapDrawable getBitmapFromMemCache(String url) {
    	BitmapDrawable bitmapDrawable = null;

    	if (mMemoryCache != null) {
    		bitmapDrawable = mMemoryCache.get(url);
    	}

    	if (bitmapDrawable != null) {
            Log.d("TEST", "Memory cache hit");
        }

    	return bitmapDrawable;
    }

    /**
     * @param data
     * @param value
     */
    public void addBitmapToCache(String url, BitmapDrawable bitmapDrawable) {
    	if (url == null || bitmapDrawable == null) {
    		return;
    	}

    	if (mMemoryCache != null) {
    		if (RecyclingBitmapDrawable.class.isInstance(bitmapDrawable)) {
    			((RecyclingBitmapDrawable) bitmapDrawable).setIsCached(true);
    		}

    		mMemoryCache.put(url, bitmapDrawable);
    	}
    }

	private static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
		RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(ImageCache.class.getName());

		if (mRetainFragment == null) {
			mRetainFragment = new RetainFragment();
			fm.beginTransaction().add(mRetainFragment,
					ImageCache.class.getName()).commitAllowingStateLoss();;
		}

		return mRetainFragment;
	}

	/**
	 *
	 */
	public static class RetainFragment extends Fragment {
		private Object object;
		

		public Object getObject() {
			return object;
		}


		public void setObject(Object object) {
			this.object = object;
		}

		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setRetainInstance(true);
        }
	}
}