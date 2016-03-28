package com.ycl.androidtech.image;

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

	//缓存基本设置
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 内存缓存默认大小5MB
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;

	//内存缓存
	private LruCache<String, BitmapDrawable> mMemoryCache;

	private ImageCacheParams mCacheParams;

	//3.0后的bitmap重用机制
	private Set<SoftReference<Bitmap>> mReusableBitmaps;


	public static class ImageCacheParams {
		//mem
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;

		/***
		 * 手动设置内存缓存大小
		 * @param percent 缓存大小占最大可用内存的比例
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

		//4.4之前的版本，尺寸必须完全吻合
		if (Build.VERSION.SDK_INT < VERSION_CODES.KITKAT) {
			return candidate.getWidth() == targetOptions.outWidth
                    && candidate.getHeight() == targetOptions.outHeight
                    && targetOptions.inSampleSize == 1;
		}
		//4.4版本，可以使用比自己大的bitmap
		int width = targetOptions.outWidth / targetOptions.inSampleSize;
		int height = targetOptions.outHeight / targetOptions.inSampleSize;

		//根据图片格式，计算具体的bitmap大小
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
	 * 放在no UI的fragment中，保证屏幕旋转时不被回收
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
    		//Android3.0以上可以重用bitmap内存，将内存缓存中的回收项重用。
    		if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
    			mReusableBitmaps =
                        Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
    		}

    		mMemoryCache = new LruCache<String, BitmapDrawable>(mCacheParams.memCacheSize) {

				@Override
				protected void entryRemoved(boolean evicted, String key,
						BitmapDrawable oldValue, BitmapDrawable newValue) {
					if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
						//减少缓存计数
						((RecyclingBitmapDrawable) oldValue).setIsCached(false);
					} else {
						//加入待重用集合
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

    /**
     * 清空缓存
     */
    public void clearCache() {
    	if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }


    /**
     * 获取bitmap大小
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
    	//3.1及以上
    	if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
    		return  bitmap.getByteCount();
    	}
    	//3.1之前的版本
    	return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**获取内存缓存中的图片
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
     * 图片加入内存缓存
     * @param data
     * @param value
     */
    public void addBitmapToCache(String url, BitmapDrawable bitmapDrawable) {
    	if (url == null || bitmapDrawable == null) {
    		return;
    	}

    	//先加入内存缓存
    	if (mMemoryCache != null) {
    		//如果是RecyclingBitmapDrawable，增加缓存计数
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
	 * 后台Fragment用于在横竖屏切换时保存ImageCache对象。
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