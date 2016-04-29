package com.android.androidtech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;


import com.android.androidtech.R;
import com.android.androidtech.activity.base.BaseFragmentActivity;
import com.android.androidtech.activity.base.StackLayout;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.fragment.homepage.HomePageFragment;
import com.android.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class HomePageActivity extends BaseFragmentActivity {

    private final static String TAG = "MusicMainWithMiniBarActivity";
    private int mFocusedTextColor, mTextColor;
    private StackLayout mMainFragmentContainer;
    private static final String MAIN_FRAGMENT_CONTENT = "main_content";
    public static final int CONTAINER_ID = R.id.homepage_fragment_detail;
    private int mViewIndex = TAB_VIEW_00;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mMainFragmentContainer = (StackLayout) findViewById(R.id.homepage_fragment_detail);
        makeNewContentFragmentStackManager(CONTAINER_ID, MAIN_FRAGMENT_CONTENT, mMainFragmentContainer);
        Bundle data = new Bundle();
        data.putInt(APP_INDEX_KEY, mViewIndex);
        addSecondFragment(HomePageFragment.class, data, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                BaseFragment top = top();
                if (top != null && top.onKeyDown(keyCode, event)) {
                    return true;
                }
                if (size() > 1) {
                    popBackStack();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private static final int MSG_REFRESH_MUSIC_CIRCLE = 10000;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLog.d(TAG, "On Resume" + getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        GLog.d(TAG, "onPause " + getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
