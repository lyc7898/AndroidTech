package com.android.androidtech.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.utils.GLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class FragmentStackManager implements IFragmentStackManager {
    private static final String TAG = "FlipperContentFragmentStack44";
    public static final int STACK_SIZE = 10;
    private StackLayout mFlipperContent;

    private ArrayList<BaseFragment> mFragmentStack;
    private int mSize;
    private final int MAX = 10 ;
    private FragmentManager mManager;
    private BaseFragment mTopFragment;

    private Activity mActivity;
    private ClearAsyncTask mClearAsyncTask;

    private int mContentId;

    private String mContentName;

    private boolean isPushOrPop = false;

    private final Object mPushLock = new Object();

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     *
     * @param activity
     *            上下文
     * @param manager
     *            FragmentManager
     */
    public FragmentStackManager(Activity activity, FragmentManager manager, int contentId,
                                String contentName, StackLayout view) {
        mFragmentStack = new ArrayList<BaseFragment>(STACK_SIZE);
        mSize = 0;
        mManager = manager;
        mContentId = contentId;
        mContentName = contentName;

        mActivity = activity;
        mFlipperContent = view;

        mFlipperContent.setOnStackAnimationListener(new StackLayout.OnStackAnimationListener() {
            @Override
            public void onStackPushAnimationEnd() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setFlipperHeadMode(mTopFragment);
                        if (mTopFragment != null) {
                            mTopFragment.onEnterAnimationEnd(null);
                        }


                        //
                        if (mFragmentStack.size() > 1) {
                            BaseFragment hideFragment = mFragmentStack
                                    .get(mFragmentStack.size() - 2);
                            hideFragment.onPause();
                        }
                        synchronized (mPushLock) {
                            isPushOrPop = false;
                        }
                    }
                });
            }

            @Override
            public void onStackPushAnimationStart() {

            }

            @Override
            public void onStackPopAnimationEnd() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setFlipperHeadMode(mTopFragment);

                        if (mFragmentStack.size() > 0) {
                            BaseFragment topFragment = mFragmentStack
                                    .get(mFragmentStack.size() - 1);
                            topFragment.onResume();
                        }

                        synchronized (mPushLock) {
                            isPushOrPop = false;
                        }
                    }
                });

            }

            @Override
            public void onStackPopAnimationStart() {
            }
        });

    }

    public boolean empty() {
        return mSize == 0;
    }

    private void setFlipperHeadMode(BaseFragment fragment) {
    }


    public void clear() {
        if (empty()) {
            return;
        }

        mFlipperContent.removeAllViews();
        mClearAsyncTask = new ClearAsyncTask();
        mClearAsyncTask.execute();
    }

    public boolean pop(int requestCode, int resultCode, Intent data) {
        if (empty() || mFlipperContent.isAnimating() || isPushOrPop()) {
            return false;
        }
        synchronized (mPushLock) {
            isPushOrPop = true;
        }
        int index = mFragmentStack.size() - 1;
        final BaseFragment _fragment = mFragmentStack.remove(index);
        _fragment.buildDrawCacheBitmap();
        mManager.beginTransaction().remove(_fragment).commitAllowingStateLoss();
        sync();
        if (mSize == 1) {

        }
        if (mTopFragment != null) {
            mTopFragment.onActivityResult(requestCode, resultCode, data);
        }
        return true;
    }

    private boolean isPushOrPop() {
        synchronized (mPushLock) {
            return isPushOrPop;
        }
    }

    public void push(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> hashMap) {
        if (mActivity.isFinishing() || mFlipperContent.isAnimating() || isPushOrPop())
            return;
        synchronized (mPushLock) {
            isPushOrPop = true;
        }
        try {
            BaseFragment _fragment = cls.newInstance();
            _fragment.setArguments(args);
            FragmentTransaction ft = mManager.beginTransaction();
            ft.add(mContentId, _fragment, mContentName);

            if (full()) {
                BaseFragment second = mFragmentStack.remove(2);
                ft.remove(second);
            }
            mFragmentStack.add(_fragment);
            if (!mActivity.isFinishing())
                ft.commitAllowingStateLoss();
            sync();
        } catch (IllegalAccessException e) {
            GLog.e(TAG, e);
        } catch (InstantiationException e) {
            GLog.e(TAG, e);
        }catch(Exception e){
            GLog.e(TAG, e);
        }
    }

    public boolean full() {
        return mSize == MAX;
    }

    public int size() {
        GLog.d(TAG, "Fragment mSize is:" + mSize + " and mFragmentStack.size() is:" + mFragmentStack.size());
        return mSize;
    }

    private synchronized void sync() {
        mSize = mFragmentStack.size();
        if (mSize == 0) {
            mTopFragment = null;
        } else {
            mTopFragment = mFragmentStack.get(mSize - 1);
            assert mTopFragment == mFragmentStack.get(mSize - 1);
        }
        assert mSize == mFragmentStack.size();
    }

    public BaseFragment top() {
        return mTopFragment;
    }

    public BaseFragment getSecondFragment() {
        return mSize >= 2 ? mFragmentStack.get(mSize - 2) : null;
    }

    public void destroy() {

    }

    private class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            FragmentTransaction transaction = mManager.beginTransaction();
            for (BaseFragment f : mFragmentStack) {
                transaction.remove(f);
            }
            transaction.commitAllowingStateLoss();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mFragmentStack.clear();
            sync();
            System.gc();
        }
    }
}
