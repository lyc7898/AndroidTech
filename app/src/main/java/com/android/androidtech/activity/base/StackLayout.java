package com.android.androidtech.activity.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;


import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.utils.GLog;
import com.android.androidtech.utils.Util4Phone;

import java.util.List;
import java.util.Stack;

/**
 * 实现层叠显示内容的控件
 *
 * @author yuchengluo
 */
public class StackLayout extends ViewGroup {

    private static final String TAG = "StackLayout";


    private static final int ANIMATION_TIME = 200;

    private static final int MAX_ALPHA = 100;

    // using to draw the animation

    private List<View> mViewStack;
    private View mTop;
    private View mPreviousTop;

    private Paint mShadePaint = new Paint();
    private Paint mDrawPaint = new Paint();

    private int mLeft;
    //private int mAlpha;
    private int mWidth;


    private boolean mAnimating = false;

    private OnStackAnimationListener mOnStackAnimationListener;

    private int mHeight;
    private Bitmap mTopCache;
    private Bitmap mPTopCache;

    private Scroller mRemoveScroller = null;
    private Scroller mAddScroller = null;

    private static final Object ANIMATION_LOCK = new Object();

    public StackLayout(Context context) {
        this(context, null, 0);
    }

    public StackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mViewStack = new Stack<View>();
        mShadePaint.setColor(0x00000000);

        final float density = getResources().getDisplayMetrics().density;

        setClickable(true);
    }

    public View top() {
        if (mViewStack.size() == 0) {
            return null;
        }
        return mViewStack.get(mViewStack.size() - 1);
    }

    public int size() {
        return mViewStack.size();
    }

    public void clear() {
        mViewStack.clear();
        removeAllViews();
    }

    public void setOnStackAnimationListener(OnStackAnimationListener listener) {
        mOnStackAnimationListener = listener;
    }

    public boolean isAnimating() {
        synchronized (ANIMATION_LOCK) {
            return mAnimating;
        }
    }

    @Override
    public void addView(View child) {
        GLog.d(TAG, "addView");
        if (!Util4Phone.isSupportAnimation()) {
            mPreviousTop = mTop;
            mTop = child;
            mViewStack.add(child);
            super.addView(child);
            if (mOnStackAnimationListener != null) {
                mOnStackAnimationListener.onStackPushAnimationEnd();
            }

            return;
        }
        if (isAnimating()) {
            return;
        }
        synchronized (ANIMATION_LOCK) {
            mAnimating = true;
        }
        mPreviousTop = mTop;
        mTop = child;
        mViewStack.add(child);
        super.addView(child);

        prepareContent();

        if (mViewStack.size() == 1) {
            if (mOnStackAnimationListener != null) {
                mOnStackAnimationListener.onStackPushAnimationEnd();
            }
            synchronized (ANIMATION_LOCK) {
                mAnimating = false;
            }
            return;
        }
        mLeft = mWidth;
        mAddScroller = new Scroller(getContext(), new LinearInterpolator());
        mAddScroller.startScroll(mLeft, 0, -mWidth, 0, ANIMATION_TIME);
        invalidate();
        if (mOnStackAnimationListener != null) {
            mOnStackAnimationListener.onStackPushAnimationStart();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mRemoveScroller != null) {
            if (!mRemoveScroller.isFinished()) {
                if (mRemoveScroller.computeScrollOffset()) {
                    mLeft = mRemoveScroller.getCurrX();
                    postInvalidate();
                    return;
                }
            } else {
                mRemoveScroller = null;
                mLeft = mWidth;
                postRemoveView();
                postInvalidate();
                synchronized (ANIMATION_LOCK) {
                    mAnimating = false;
                }
            }
        }
        if (mAddScroller != null) {
            if (!mAddScroller.isFinished()) {
                if (mAddScroller.computeScrollOffset()) {
                    mLeft = mAddScroller.getCurrX();
                    postInvalidate();
                    return;
                }
            } else {
                mAddScroller = null;
                mLeft = 0;
                postInvalidate();
                if (mOnStackAnimationListener != null) {
                    mOnStackAnimationListener.onStackPushAnimationEnd();
                }
                synchronized (ANIMATION_LOCK) {
                    mAnimating = false;
                }
            }
        }
    }
    @Override
    public void removeView(View view) {
        GLog.d(TAG, "removeView");
        if (!Util4Phone.isSupportAnimation()) {
            postRemoveView();
            return;
        }

        if (isAnimating()) {
            return;
        }
        synchronized (ANIMATION_LOCK) {
            mAnimating = true;
        }
        if (view != null) {
            if (mViewStack.size() == 1) {
                // won't do animation
                mViewStack.remove(mTop);
                mPreviousTop = null;
                mTop = null;
                super.removeView(mTop);
                invalidate();
                synchronized (ANIMATION_LOCK) {
                    mAnimating = false;
                }
                if (mOnStackAnimationListener != null) {
                    mOnStackAnimationListener.onStackPopAnimationEnd();
                }
                return;
            } else if (mViewStack.size() > 3 && view.equals(mViewStack.get(2))) {
                mViewStack.remove(view);
                super.removeView(view);
                invalidate();
                synchronized (ANIMATION_LOCK) {
                    mAnimating = false;
                }
                return;
            } else if (!view.equals(mTop)) {
                mViewStack.remove(view);
                super.removeView(view);
                mViewStack.remove(mTop);
                super.removeView(mTop);
                mTop = top();
                if (mViewStack.size() >= 2) {
                    mPreviousTop = mViewStack.get(mViewStack.size() - 2);
                } else {
                    mPreviousTop = null;
                }
                invalidate();
                synchronized (ANIMATION_LOCK) {
                    mAnimating = false;
                }
                if (mOnStackAnimationListener != null) {
                    mOnStackAnimationListener.onStackPopAnimationEnd();
                }
                return;
            }
        }

        assert view != null;
        Object useAnimation = view.getTag(1);
        if (useAnimation != null && useAnimation instanceof Boolean && !(Boolean) useAnimation) {
            postRemoveView();
            synchronized (ANIMATION_LOCK) {
                mAnimating = false;
            }
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            postRemoveView();
            synchronized (ANIMATION_LOCK) {
                mAnimating = false;
            }
            return;
        }
        prepareContent();
        mLeft = 0;
        long now = SystemClock.uptimeMillis();
        mRemoveScroller = new Scroller(getContext(), new LinearInterpolator());
        mRemoveScroller.startScroll(mLeft, 0, mWidth, 0, ANIMATION_TIME);
        invalidate();

        if (mOnStackAnimationListener != null) {
            mOnStackAnimationListener.onStackPopAnimationStart();
        }
    }

    private void postRemoveView() {
        mViewStack.remove(mTop);
        super.removeView(mTop);
        mTop = top();
        if (mViewStack.size() >= 2) {
            mPreviousTop = mViewStack.get(mViewStack.size() - 2);
        } else {
            mPreviousTop = null;
        }
        mLeft = 0;
        if (mOnStackAnimationListener != null) {
            mOnStackAnimationListener.onStackPopAnimationEnd();
        }
        invalidate();
    }

    @Override
    public void removeAllViews() {
        mViewStack.clear();
        mPreviousTop = null;
        mTop = null;
        super.removeAllViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // loop children for measure
        if (mTop != null) {
            if (mTop.getLayoutParams() == null) {
                mTop.setLayoutParams(new LayoutParams(widthSize, heightSize));
            }
            measureChild(mTop, MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mWidth = r - l;
        mHeight = b - t;

        // loop children for layout
        if (changed) {
            for (View v : mViewStack) {
                v.layout(0, 0, mWidth, mHeight);
            }
        } else if (mTop != null) {
            mTop.layout(0, 0, mWidth, mHeight);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        GLog.d(TAG, "dispatchKeyEvent");
        if (isAnimating()) {
            return true;
        }
        final View top = mTop;
        if (top != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return top.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
    @Override
    public void setVisibility(int visibility) {
        if (mTop != null) {
            mTop.setVisibility(visibility);
        }
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 如果在动画中，绘制一个白色背景就行了，没必要调用super.dispatchDraw(canvas)
        if (mTop != null) {
            try {
                BaseFragment fragment = (BaseFragment) mTop.getTag();
                GLog.i(TAG, "dispatchDraw mTop = " + fragment.getClass().getName());
            } catch (Throwable e) {
            }
        }
        if (isAnimating()) {
            canvas.drawColor(Color.WHITE);
        }
        final long drawingTime = getDrawingTime();
        if (mTop != null) {
            if (isAnimating()) {
                if (mPreviousTop != null) {
                    int previousLeft = (-mWidth + mLeft) / 4;

                    if (mPTopCache != null && !mPTopCache.isRecycled()) {
                        canvas.drawBitmap(mPTopCache, previousLeft, 0, mDrawPaint);
                    } else {
                        canvas.save();
                        canvas.translate(previousLeft, 0);
                        drawChild(canvas, mPreviousTop, drawingTime);
                        canvas.restore();
                    }
                }
                canvas.drawRect(0, 0, mLeft, mHeight, mShadePaint);
                if (mTopCache != null && !mTopCache.isRecycled()) {
                    canvas.drawBitmap(mTopCache, mLeft, 0, mDrawPaint);
                } else {
                    canvas.save();
                    canvas.translate(mLeft, 0);
                    drawChild(canvas, mTop, drawingTime);
                    canvas.restore();
                }
            } else {
                drawChild(canvas, mTop, drawingTime);
                if (mTopCache != null && !mTopCache.isRecycled()) {
                    mTopCache.recycle();
                    GLog.d(TAG, "clear mTopCache");
                }
                mTopCache = null;
                if (mPTopCache != null && !mPTopCache.isRecycled()) {
                    mPTopCache.recycle();

                    GLog.d(TAG, "clear mPTopCache");
                }
                mPTopCache = null;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAnimating()) {
            return false;
        }
        if (mTop != null) {
            return mTop.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void prepareContent() {
        if (mTop == null) {
            return;
        }
        if (mTopCache != null && !mTopCache.isRecycled()) {
            mTopCache.recycle();
            mTopCache = null;
        }
        if (mPTopCache != null && !mPTopCache.isRecycled()) {
            mPTopCache.recycle();
            mPTopCache = null;
        }
        if (mTop != null) {
            if (mTop.getTag() != null && mTop.getTag() instanceof Bitmap) {
                mTopCache = (Bitmap) mTop.getTag();
                mTop.setTag(null);
            }
        }
        if (mPreviousTop != null) {
            if (mPreviousTop.getTag() != null && mPreviousTop.getTag() instanceof Bitmap) {
                mPTopCache = (Bitmap) mPreviousTop.getTag();
                mPreviousTop.setTag(null);
            }
        }
    }

    public interface OnStackAnimationListener {
        public void onStackPushAnimationEnd();
        public void onStackPushAnimationStart();
        public void onStackPopAnimationEnd();
        public void onStackPopAnimationStart();
    }
}
