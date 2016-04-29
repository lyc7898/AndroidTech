package com.android.androidtech.fragment.performance.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.android.androidtech.R;
import com.android.androidtech.activity.HomePageActivity;
import com.android.androidtech.activity.ex.LayoutPerActivity;
import com.android.androidtech.fragment.base.BaseFragment;

/**
 * Created by yuchengluo on 2015/7/16.
 * UI Home page Fragment
 */
public class UiPerfFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtn_OverDraw,mBtn_Xml,mBtn_ListView,mBtn_Merge;
    private Context mContext = null;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getHostActivity();
        View view = inflater.inflate(R.layout.fm_ui_perf, container, false);
        mBtn_OverDraw = (Button)view.findViewById(R.id.btn_overdraw);
        mBtn_OverDraw.setOnClickListener(this);
        mBtn_Xml = (Button)view.findViewById(R.id.btn_xml);
        mBtn_Xml.setOnClickListener(this);
        mBtn_ListView = (Button)view.findViewById(R.id.btn_listview);
        mBtn_ListView.setOnClickListener(this);
        mBtn_Merge = (Button)view.findViewById(R.id.btn_merge_layout);
        mBtn_Merge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getHostActivity(), LayoutPerActivity.class);
                ((HomePageActivity) mContext).startActivity(it);
            }
        });
        return view;
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
    public void onClick(View v) {
        if(v.getId() == mBtn_OverDraw.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(OverDrawFragment.class, mBundle, null);
        }else if(v.getId() == mBtn_Xml.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(ViewStubDemoFragment.class, mBundle, null);
        }else if(v.getId() == mBtn_ListView.getId()){
            Bundle mBundle = new Bundle();
            ((HomePageActivity) mContext).addSecondFragment(ListViewFragment.class, mBundle, null);
        }
    }
}
