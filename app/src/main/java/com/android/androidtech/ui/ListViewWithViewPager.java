package com.android.androidtech.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewWithViewPager extends ListView {

	private static final String TAG = "ListViewWithViewPager";
	private static final int DISTANCE_X_SCROLL = 10;

	private View mHeaderView = null;
	private float mHeaderWidth = 0;
	private float mHeaderHeight = 0;

	public ListViewWithViewPager(Context context) {
		super(context);
	}

	public ListViewWithViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ListViewWithViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * touch事件分发
	 * <p>
	 * 在该方法中，将对触控到非HeaderView的事件进行拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		// boolean result;
		if (ev != null) {

			int action = ev.getAction();
			// MLog.d(TAG, "onInterceptTouchEvent action:"+action);

			if (MotionEvent.ACTION_DOWN == action) {

				if (mHeaderView != null) {

					mHeaderWidth = mHeaderView.getWidth();
					mHeaderHeight = mHeaderView.getHeight();

					View firstVisibleView = this.getChildAt(getFirstVisiblePosition());
					final float firstVisibleViewLeftY;
					if (firstVisibleView != null && firstVisibleView == mHeaderView) {

						if (Math.abs(firstVisibleView.getTop()) < mHeaderHeight) {

							firstVisibleViewLeftY = mHeaderHeight - (Math.abs(firstVisibleView.getTop()));

							// MLog.d(TAG,
							// "mHeaderWidth :"+mHeaderWidth+"  mHeaderHeight:"+mHeaderHeight);
							if (mHeaderHeight != 0 && mHeaderWidth != 0) {
								// final float downX = ev.getX();
								final float downY = ev.getY();
								// MLog.d(TAG, "ev X:"+downX+"  y:"+downY);
								// MLog.d(TAG,
								// "firstVisibleViewLeftY:"+firstVisibleViewLeftY);

								// 触控的区域为HeaderView区域，则不拦截事件
								if (firstVisibleViewLeftY > downY) {

									// MLog.d(TAG, "ListView不截获Touch事件");

									return false;
								}

							}
						}
					}

				}
			}
		}

		// MLog.d(TAG, "ListView截获Touch事件");
		if (ev != null) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}

	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		super.addHeaderView(v, data, isSelectable);
		handlerView(v);
	}

	@Override
	public void addHeaderView(View v) {
		super.addHeaderView(v);
		handlerView(v);
	}

	private void handlerView(View v) {
		mHeaderView = v;
	}

	public View getHandlerHeaderView() {
		return mHeaderView;
	}
}
