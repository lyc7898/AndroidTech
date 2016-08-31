package com.android.androidtech.fragment.homepage;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.androidtech.R;
import com.android.androidtech.config.PageIndexer;
import com.android.androidtech.fragment.PerformanceFragment;
import com.android.androidtech.fragment.TestThirdFragment;
import com.android.androidtech.fragment.UiPerfMoniterFragment;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.fragment.base.MyFragmentPagerAdapter;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class HomePageFragment extends BaseFragment implements PageIndexer {
    private final static String TAG = "MainDesktopFragment";
    public final static int MSG_REFRESH_BANNER = 1;
    public final static String IS_SHOW_PUSH_DIALOG = "is_show_dialog";
    public final static String PUSH_DIALOG_TITLE = "dialog_title";
    public final static String PUSH_DIALOG_MSG = "dialog_message";
    private int mViewIndex = TAB_VIEW_00;
    private MyFragmentStatePagerAdapter mAdapter;
    private BaseFragment[] mFragments;
    public GmfBaseViewPager mPagerDetail;
    public ImageView mSearchBtn;
    public FrameLayout mMoreBtn;
    public TextView mPerfTab;
    public TextView mMusicHallTab;
    public TextView mFindTab;
    public ImageView mPerfNewFlag;
    public ImageView mFindNewFlag;
    public ImageView mMoreNewFlag;
    private FragmentManager mFragmentManager;

    private boolean mIsFirstIn = true;

    public static final int MY_MUSIC_NEW_FLAG = 0x0001;

    public static final int FIND_NEW_FLAG = 0x0002;

    public static final int MORE_NEW_FLAG = 0x0003;

    public interface OnNewFlagChangedLitener {

        public void onNewFlagShow(int newFlag);

        public void onNewFlagHide(int newFlag);
    }

    private OnNewFlagChangedLitener mOnNewFlagChangedLitener = new OnNewFlagChangedLitener() {

        @Override
        public void onNewFlagShow(int newFlag) {
            switch (newFlag) {
                case MY_MUSIC_NEW_FLAG:
                    mPerfNewFlag.setVisibility(View.VISIBLE);
                    break;
                case FIND_NEW_FLAG:
                    mFindNewFlag.setVisibility(View.VISIBLE);
                    break;
                case MORE_NEW_FLAG:
                    mMoreNewFlag.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onNewFlagHide(int newFlag) {
            switch (newFlag) {
                case MY_MUSIC_NEW_FLAG:
                    mPerfNewFlag.setVisibility(View.GONE);
                    break;
                case FIND_NEW_FLAG:
                    mFindNewFlag.setVisibility(View.GONE);
                    break;
                case MORE_NEW_FLAG:
                    mMoreNewFlag.setVisibility(View.GONE);
                    break;
            }
        }

    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getHostActivity().getLayoutInflater().inflate(R.layout.fragment_homepage, null);
        mPagerDetail = (GmfBaseViewPager)view.findViewById(R.id.main_desk_fragment_pager);
        mSearchBtn = (ImageView)view.findViewById(R.id.main_desk_title_search_btn);
        mMoreBtn =  (FrameLayout)view.findViewById(R.id.main_desk_title_more_btn);
        mPerfTab = (TextView)view.findViewById(R.id.main_desk_title_tab_mymusic);
        mMusicHallTab = (TextView)view.findViewById(R.id.main_desk_title_tab_musichall);
        mFindTab = (TextView)view.findViewById(R.id.main_desk_title_tab_find);
        mPerfNewFlag = (ImageView)view.findViewById(R.id.homepage_tab_performance_new_flag);
        mFindNewFlag = (ImageView)view.findViewById(R.id.main_desk_title_tab_find_new_flag);
        mMoreNewFlag = (ImageView)view.findViewById(R.id.main_desk_title_more_new_flag);

        initView();
        return view;
    }

    private void initView() {
        mFragments = new BaseFragment[3];
        PerformanceFragment fragment1 = new PerformanceFragment();
        mFragments[0] = fragment1;
        fragment1.setRetainInstance(true);
        UiPerfMoniterFragment fragment2 = new UiPerfMoniterFragment();
        fragment2.setRetainInstance(true);
        fragment2.setBg(Color.RED);
        mFragments[1] = fragment2;
        TestThirdFragment fragment3 = new TestThirdFragment();
        fragment3.setRetainInstance(true);
        mFragments[2] = fragment3;
        mAdapter = new MyFragmentStatePagerAdapter(mFragmentManager);
        new setAdapterTask().execute();
        mPagerDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < mAdapter.getCount() && position != mViewIndex) {
                    setSelectedTab(position);
                }
                BaseFragment f = mFragments[position];
                if (position == 0) {
                    mPerfNewFlag.setVisibility(View.GONE);
                } else if (position == 2) {
                    mFindNewFlag.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 滑动pager的时候触发

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPerfTab.setOnClickListener(mTabClickListener);
        mMusicHallTab.setOnClickListener(mTabClickListener);
        mFindTab.setOnClickListener(mTabClickListener);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mMoreBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    private View.OnClickListener mTabClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int position = 0;
            if (v == mPerfTab) {
                position = 0;
            } else if (v == mMusicHallTab) {
                position = 1;
            } else if (v == mFindTab) {
                position = 2;
            }
            setSelectedTab(position);
            mPagerDetail.setCurrentItem(position);
        }
    };

    public void gotoSelectedTab(int position) {
        setSelectedTab(position);
        mPagerDetail.setCurrentItem(position);
    }

    private void setSelectedTab(int position) {
        mViewIndex = position;
        mPerfTab.setTextColor(0x66FFFFFF);
        mMusicHallTab.setTextColor(0x66FFFFFF);
        mFindTab.setTextColor(0x66FFFFFF);
        switch (mViewIndex) {
            case 0:
                mPerfTab.setTextColor(0xFFFFFFFF);
                break;
            case 1:
                mMusicHallTab.setTextColor(0xFFFFFFFF);;
                break;
            case 2:
                mFindTab.setTextColor(0xFFFFFFFF);
                break;
        }
    }

    @Override
    protected void resume() {
        if (mIsFirstIn) {
            mIsFirstIn = false;
            return;
        }

        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onResume();
            }
        }
    }

    public BaseFragment getViewPage(int index) {
        if (mFragments != null && index >= 0 && index < mFragments.length) {
            return mFragments[index];
        }
        return null;
    }

    @Override
    protected void stop() {
        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onStop();
            }
        }

        mIsFirstIn = true;
    }

    @Override
    protected void pause() {
        for (BaseFragment f : mFragments) {
            if (f.isAdded()) {
                f.onPause();
            }
        }
    }

    @Override
    protected void start() {
        for (Fragment f : mFragments) {
            if (f.isAdded()) {
                f.onStart();
            }
        }
    }

    @Override
    public void onEnterAnimationEnd(Animation animation) {

    }

    @Override
    public void clearView() {

    }

    @Override
    public void clear() {

    }

    @Override
    protected void initData(Bundle data) {
        mFragmentManager = getChildFragmentManager();
        int gotoAppIndex = data.getInt(APP_INDEX_KEY, TAB_VIEW_00);
        if (gotoAppIndex != TAB_VIEW_00 && (gotoAppIndex - APP_MAIN_1) >= TAB_VIEW_00
                && (gotoAppIndex - APP_MAIN_1) < TAB_VIEW_02 + 1) {
            mViewIndex = gotoAppIndex - APP_MAIN_1;
        }
        else {
            mViewIndex = TAB_VIEW_00;
        }
    }

    public class MyFragmentStatePagerAdapter extends MyFragmentPagerAdapter {
        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private class setAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPagerDetail.setAdapter(mAdapter);
            mPagerDetail.setOffscreenPageLimit(mAdapter.getCount() + 1);
            mPagerDetail.postDelayed(new Runnable() {

                @Override
                public void run() {
                    gotoSelectedFragment();
                }
            }, 300);
        }
    }

    private void gotoSelectedFragment() {
        if (mFragments != null && mFragments.length > mViewIndex) {
            setSelectedTab(mViewIndex);
            mPagerDetail.setCurrentItem(mViewIndex);
        }
    }
}
