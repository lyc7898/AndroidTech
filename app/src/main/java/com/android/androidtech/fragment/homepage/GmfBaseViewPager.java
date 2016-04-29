package com.android.androidtech.fragment.homepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.androidtech.ui.ListViewWithViewPager;
import com.android.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2015/6/29.
 */
@SuppressLint("UseSparseArrays")
public class GmfBaseViewPager extends ViewPager {

    private final static String TAG = "QMusicBaseViewPager";

    private boolean mIsNoDown = false;

    private boolean isViewPagerScroll = true;

    private ListViewWithViewPager mInterceptView;

    private GmfBaseViewPager mPager;

    public void setViewPagerScroll(boolean isViewPagerScroll) {
        this.isViewPagerScroll = isViewPagerScroll;
    }

    public boolean isViewPagerScroll() {
        return isViewPagerScroll;
    }

    public GmfBaseViewPager(Context context) {
        this(context, null);
    }

    public GmfBaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isViewPagerScroll()) {
            return false;
        }
        if (mInterceptView != null) {
            int action = ev.getAction();
            if (mIsNoDown) {
                if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
                    mIsNoDown = false;
                }
                return false;
            }
            if (MotionEvent.ACTION_DOWN == action) {

                View mHeaderView = mInterceptView.getHandlerHeaderView();

                if (mHeaderView != null) {
                    int mHeaderWidth = mHeaderView.getWidth();
                    int mHeaderHeight = mHeaderView.getHeight();

                    View firstVisibleView = mInterceptView.getChildAt(mInterceptView.getFirstVisiblePosition());
                    final float firstVisibleViewLeftY;
                    if (firstVisibleView != null && firstVisibleView == mHeaderView) {

                        if (Math.abs(firstVisibleView.getTop()) <= mHeaderHeight) {

                            firstVisibleViewLeftY = mHeaderHeight - (Math.abs(firstVisibleView.getTop()));
                            if (mHeaderHeight != 0 && mHeaderWidth != 0) {
                                final float downY = ev.getY();
                                // 触控的区域为HeaderView区域，则不拦截事件
                                if (firstVisibleViewLeftY + 20 > downY) {
                                    mIsNoDown = true;
                                    return false;
                                }

                            }
                        }
                    }

                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isViewPagerScroll()) {
            return false;
        }
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            GLog.e(TAG, "onTouchEvent e is:" + e);
            return false;
        }
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (mPager != null) {
            if (mPager.isCanHorScroll(dx)) {
                return true;
            }
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    public boolean isCanHorScroll(int direction) {
        if (getAdapter() == null) {
            return false;
        }
        int count = getAdapter().getCount();
        if (direction > 0) {
            return getCurrentItem() > 0;
        } else {
            return getCurrentItem() < count - 1;
        }
    }

    public View getCanScrollView(int index) throws IndexOutOfBoundsException {
        View v = null;
        PagerAdapter adapter = getAdapter();
        if (adapter == null) {
            return null;
        }

        int count = adapter.getCount();
        if (count <= index) {
            return null;
        }

        if (adapter instanceof FragmentStatePagerAdapter) {
            v = ((ViewGroup) ((FragmentStatePagerAdapter) adapter).getItem(index).getView()).getChildAt(0);
        } else if (adapter instanceof FragmentPagerAdapter) {
            ViewGroup parent = (ViewGroup) ((FragmentPagerAdapter) adapter).getItem(index).getView();
            if (parent != null && parent.getChildCount() > 0) {
                v = parent.getChildAt(0);
            }
        }
//        else if (adapter instanceof MyFragmentPagerAdapter) {
//            Fragment f = ((MyFragmentPagerAdapter) adapter).getItem(index);
//            if (f != null) {
//                ViewGroup parent = (ViewGroup) f.getView();
//                if (parent != null && parent.getChildCount() > 0) {
//                    v = parent.getChildAt(0);
//                }
//            }
//            //使用viewstub以后，无法使用父控件找到webview
//            if(f instanceof BaseWebViewFragment){
//                BaseWebViewFragment w = (BaseWebViewFragment)f;
//                return  w.getWebView();
//            }
//
//        }
        else {
            v = getChildAt(index);
        }


        if (v == null) {
            return null;
        }
       if (v instanceof ViewGroup) {
            count = ((ViewGroup) v).getChildCount();
            for (int i = 0; i < count; i++) {
                View cv = ((ViewGroup) v).getChildAt(i);
                if (cv instanceof ScrollView || cv instanceof ListView) {
                    return cv;
                }
            }
        }
        return null;
    }

    public void clear() {
    }

    public void setInterceptView(ListViewWithViewPager view) {
        mInterceptView = view;
    }

    public void setViewPager(GmfBaseViewPager pager) {
        mPager = pager;
    }
}
