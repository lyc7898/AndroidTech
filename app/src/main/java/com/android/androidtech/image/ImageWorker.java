package com.android.androidtech.image;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
/***
 *
 * ListView上每个item启动一个AsyncTask去下载图片，AsyncTask在容量为2的线程池上运行。
 * 每个AsyncTask和item上的下载url和imageview绑定
 *
 */
public abstract class ImageWorker {

	private boolean mExitTasksEarly = false;  // 是否提前退出的标志
	protected boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();

	protected Resources mResources;

	private ImageCache mImageCache; //����
	private ImageCache.ImageCacheParams mImageCacheParams;

	protected ImageWorker(Context context) {
		mResources = context.getResources();
	}

	public ImageCache getImageCache() {
		return mImageCache;
	}

	public void initImageCache(FragmentManager fragmentManager,
            ImageCache.ImageCacheParams cacheParams) {
    	mImageCacheParams = cacheParams;
    	mImageCache = ImageCache.getInstance(fragmentManager, mImageCacheParams);

    }

	public void loadImage(String url, ImageView imageView) {
		if (url == null) {
			return;
		}

		BitmapDrawable bitmapDrawable = null;

		//先从内存缓存中读取
        if (mImageCache != null) {
        	bitmapDrawable = mImageCache.getBitmapFromMemCache(url);
        }

        if (bitmapDrawable != null) {
        	imageView.setImageDrawable(bitmapDrawable);
        }
		//否则下载
        else if (cancelPotentialWork(url, imageView)) {

			final BitmapWorkerTask task = new BitmapWorkerTask(url, imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources,
					task);
			imageView.setImageDrawable(asyncDrawable);

			task.executeOnExecutor(ImageAsyncTask.DUAL_THREAD_EXECUTOR);
		}
	}

	/**
	 *
	 *
	 当item进行图片下载时，这个item有可能是以前回收的item，此时item上的imageView还和一个task关联着，
	 * 检查当前的task和imageview绑定的以前的task是不是在下载同一个图片， 不匹配则终止以前运行的task，否则继续之前的下载
	 *
	 * @param data
	 * @param imageView
	 * @return true表示需要重新下载
	 */
	public static boolean cancelPotentialWork(String currentUrl,
			ImageView imageView) {
		BitmapWorkerTask oldBitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (oldBitmapWorkerTask != null) {
			String oldUrl = oldBitmapWorkerTask.mUrl;
			if (oldUrl == null || !oldUrl.equals(currentUrl)) {
				oldBitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}

		return true;
	}

	/***
	 * 下载图片的task。 ListView上Item的ImageView被回收重用后，绑定的下载图片task可能和之前的不一样，
	 * Item回收可能发生在:task执行中，task执行后。这2种情况都需要校验，通过{@link #getAttachedImageView()}
	 * 方法
	 *
	 */
	private class BitmapWorkerTask extends
			ImageAsyncTask<Void, Void, BitmapDrawable> {
		private String mUrl;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(String url, ImageView imageView) {
			mUrl = url;
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected BitmapDrawable doInBackground(Void... params) {

			Bitmap bitmap = null;
			BitmapDrawable drawable = null;

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
					&& getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = processBitmap(mUrl);
			}

			if (bitmap != null) {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
                    drawable = new BitmapDrawable(mResources, bitmap);
                } else {
					// 3.0以下版本，创建自动回收的BitmapDrawable
                    drawable = new RecyclingBitmapDrawable(mResources, bitmap);
                }
				//加入缓存
				if (mImageCache != null) {
                    mImageCache.addBitmapToCache(mUrl, drawable);
                }
			}

			return drawable;
		}

		@Override
		protected void onPostExecute(BitmapDrawable result) {
			if (isCancelled() || mExitTasksEarly) {
				result = null;
			}

			ImageView imageView = getAttachedImageView();
			if (result != null && imageView != null) {

				setImageDrawable(imageView, result);
			}
		}

		@Override
		protected void onCancelled(BitmapDrawable value) {
			super.onCancelled(value);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/***
		 * 返回当前绑定task的ImageView，如果ImageView绑定的task不是自己，则返回null
		 *
		 * @return
		 */
		private ImageView getAttachedImageView() {
			ImageView imageView = imageViewReference.get();
			BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/***
	 *  * 图片下载完毕后设置ImageView
	 *
	 * @param imageView
	 * @param drawable
	 */
	private void setImageDrawable(ImageView imageView, Drawable drawable) {
		imageView.setImageDrawable(drawable);
	}

	/***
	 * 返回当前ImageView绑定的task
	 *
	 * @param imageView
	 * @return
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}

		return null;
	}

	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, BitmapWorkerTask bitmapWorkerTask) {
			super(res);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);

		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
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

	/***
	 * 下载
	 * 
	 * @param url
	 * @return
	 */
	protected abstract Bitmap processBitmap(String url);

}

