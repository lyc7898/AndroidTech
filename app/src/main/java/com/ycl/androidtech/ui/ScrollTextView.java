//package com.ycl.androidtech.ui;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.animation.LinearInterpolator;
//import android.widget.Scroller;
//import android.widget.TextView;
//
//import com.ycl.androidtech.utils.GLog;
//
///**
// * Created by yuchengluo on 2015/7/30.
// */
//public class ScrollTextView extends TextView{
//    // scrolling feature
//    private Scroller mSlr;
//
//    boolean backForward = false;
//
//    // milliseconds for a round of scrolling
//    private int mRndDuration = 20000;
//
//    // the X offset when paused
//    private int mXPaused = 0;
//
//    // whether it's being paused
//    private boolean mPaused = true;
//
//    //private boolean first = true;
//
//    /*
//     * constructor
//     */
//    public ScrollTextView(Context context) {
//        this(context, null);
//    }
//
//    /*
//     * constructor
//     */
//    public ScrollTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, android.R.attr.textViewStyle);
//    }
//
//    /*
//     * constructor
//     */
//    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        // customize the TextView
//        setSingleLine();
//        setEllipsize(null);
//        // setVisibility(INVISIBLE);
//    }
//
//    public void resetText() {
//        mXPaused = 0;
//    }
//
//    /**
//     * begin to scroll the text from the original position
//     */
//    private void startScroll() {
//        // begin from the very right side
//        mXPaused = -getWidth();
//        // assume it's paused
//        mPaused = true;
//        resumeScroll();
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        // TODO Auto-generated method stub
//        super.onLayout(changed, left, top, right, bottom);
//        // if (first) {
//        // postDelayed(new Runnable() {
//        // @Override
//        // public void run() {
//        // startScroll();
//        // }
//        //
//        // }, 2000);
//        // first = false;
//        // }
//        // startScroll();
//    }
//
//    /**
//     * resume the scroll from the pausing point
//     */
//    public void resumeScroll() {
//
//        if (!mPaused)
//            return;
//
//        // Do not know why it would not scroll sometimes
//        // if setHorizontallyScrolling is called in constructor.
//        setHorizontallyScrolling(true);
//
//        // use LinearInterpolator for steady scrolling
//        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
//        setScroller(mSlr);
//
//        int scrollingLen = calculateScrollingLen();
//        int distance = scrollingLen - (getWidth() + mXPaused);
//        GLog.d("test", "distance=" + distance);
//        GLog.d("test", "distance - getWidth()=" + (distance - 320));
//        int duration = (new Double(mRndDuration * distance * 1.00000 / scrollingLen)).intValue();
//
//        // setVisibility(VISIBLE);z
//        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
//        mPaused = false;
//        backForward = true;
//    }
//
//    public void backWardScroll() {
//        int scrollingLen = calculateScrollingLen();
//        int distance = scrollingLen;
//        // int duration = (new Double(mRndDuration * distance * 1.00000
//        // / scrollingLen)).intValue();
//        mSlr.startScroll(distance, 0, -getWidth(), 0, 0);
//        backForward = false;
//    }
//
//    /**
//     * calculate the scrolling length of the text in pixel
//     *
//     * @return the scrolling length in pixels
//     */
//    private int calculateScrollingLen() {
//        TextPaint tp = getPaint();
//        Rect rect = new Rect();
//        String strTxt = getText().toString();
//        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
//        int scrollingLen = rect.width() + getWidth();
//        rect = null;
//        return scrollingLen;
//    }
//
//    /**
//     * pause scrolling the text
//     */
//    public void pauseScroll() {
//        if (null == mSlr)
//            return;
//
//        if (mPaused)
//            return;
//
//        mPaused = true;
//
//        // abortAnimation sets the current X to be the final X,
//        // and sets isFinished to be true
//        // so current position shall be saved
//        mXPaused = 0;
//
//        mSlr.abortAnimation();
//    }
//
//    public void startScrollIfNeed(){
//        resetText();
//        if (null == getText() || getText().toString().trim().length() == 0) {
//            pauseScroll();
//            setGravity(Gravity.CENTER);
//            return;
//        }
//
//        int charNum = getMaxCharNumInternal();
//        if (this.getText().length() >= charNum && charNum != 0) {
//            setGravity(Gravity.CENTER);
//            resumeScroll();
//            setGravity(Gravity.NO_GRAVITY);
//        } else {
//            pauseScroll();
//            setGravity(Gravity.CENTER);
//        }
//    }
//
//    protected int getMaxCharNumInternal() {
//        String temp = this.getText().toString().trim();
//        TextPaint paint = this.getPaint();
//        Rect rect = new Rect();
//        paint.getTextBounds(temp, 0, temp.length(), rect);
//        double textWidthForEach = rect.width() / temp.length();
//        int tempWidth = this.getWidth();
//        if(tempWidth <= 0){
//            tempWidth = this.getMeasuredWidth();
//        }
//        int width = (int) (tempWidth - this.getPaddingLeft()-this.getPaddingRight());
//        return (int) (width / textWidthForEach);
//    }
//
//    @Override
//	/*
//	 * override the computeScroll to restart scrolling when finished so as that
//	 * the text is scrolled forever
//	 */
//    public void computeScroll() {
//        super.computeScroll();
//
//        if (null == mSlr)
//            return;
//
//        if (mSlr.isFinished() && (!mPaused) && (backForward)) {
//            backWardScroll();
//        } else if (mSlr.isFinished() && (!mPaused) && (!backForward)) {
//            startScroll();
//        }
//    }
//
//    public boolean isPaused() {
//        return mPaused;
//    }
//}
