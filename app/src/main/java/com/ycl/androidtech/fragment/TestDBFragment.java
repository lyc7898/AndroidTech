package com.ycl.androidtech.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.ycl.androidtech.R;
import com.ycl.androidtech.activity.HomePageActivity;
import com.ycl.androidtech.fragment.base.BaseFragment;


/**
 * Created by yuchengluo on 2015/6/29.
 */
public class TestDBFragment extends BaseFragment {
    Button mJump = null;
    private Context mContext = null;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView =  inflater.inflate(R.layout.fragment_test_sec, container, false);
        mJump = (Button)mView.findViewById(R.id.btn_jump_3);
        mContext = getHostActivity();
        mJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                ((HomePageActivity) mContext).addSecondFragment(TestThirdFragment.class, mBundle, null);
            }
        });
        return mView;
    }

    @Override
    protected void resume() {

    }

    @Override
    protected void stop() {

    }

    @Override
    protected void pause() {

    }

    @Override
    protected void start() {

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

    }

    @Override
    public int getFromID() {
        return 0;
    }

    @Override
    public void loginOk() {

    }

    @Override
    public void logoutOk() {

    }
}
