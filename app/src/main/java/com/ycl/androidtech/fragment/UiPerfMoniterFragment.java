package com.ycl.androidtech.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ycl.androidtech.R;
import com.ycl.androidtech.activity.HomePageActivity;
import com.ycl.androidtech.fragment.base.BaseFragment;
import com.ycl.androidtech.monitor.ui.UiPerfMonitor;

/**
 * Created by yuchengluo on 2015/6/29.
 * UI¿¨¶Ù¼à¿ØFramgent
 */
public class UiPerfMoniterFragment extends BaseFragment {
    RelativeLayout mtest = null;
    Button mJump = null;
    int mColor = Color.WHITE;
    private Context mContext = null;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_test, container, false);
        mContext = getHostActivity();
        mtest = (RelativeLayout) view.findViewById(R.id.test_bg);
        mtest.setBackgroundColor(mColor);
        mJump = (Button) view.findViewById(R.id.btn_start_perfmon);
        mJump.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         startMonitorPerf();
                                     }
                                 }

        );
        return view;
    }

    public void setBg(int color) {
        //mColor = color;
    }
    private void startMonitorPerf(){
        UiPerfMonitor.getmInstance().startMonitor();
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
