package com.android.androidtech.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.android.androidtech.R;
import com.android.androidtech.activity.HomePageActivity;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.fragment.performance.memory.BitmapMemeryFragment;
import com.android.androidtech.fragment.performance.memory.ImageGridFragment;
import com.android.androidtech.fragment.performance.ui.UiPerfFragment;

/**
 * Created by yuchengluo on 2015/7/16.
 */
public class PerformanceFragment extends BaseFragment  implements View.OnClickListener {

    private Button mBtn_UiPerf,mBtn_MenPerf ,mBtn_BitMap,mBtn_Contact= null;
    private Context mContext = null;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getHostActivity();
        View view = inflater.inflate(R.layout.fm_performance, container, false);
        mBtn_UiPerf = (Button)view.findViewById(R.id.btn_ui_perf);
        mBtn_UiPerf.setOnClickListener(this);

        mBtn_MenPerf = (Button)view.findViewById(R.id.btn_men_perf);
        mBtn_MenPerf.setOnClickListener(this);

        mBtn_BitMap = (Button)view.findViewById(R.id.btn_bitmap_men);
        mBtn_BitMap.setOnClickListener(this);

        mBtn_Contact = (Button)view.findViewById(R.id.btn_contact);
        mBtn_Contact.setOnClickListener(this);
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
       if(v.getId() == mBtn_UiPerf.getId()){
           Bundle mBundle = new Bundle();
           ((HomePageActivity) mContext).addSecondFragment(UiPerfFragment.class, mBundle, null);
       }else if(v.getId() == mBtn_MenPerf.getId()){
           Bundle mBundle = new Bundle();
           ((HomePageActivity) mContext).addSecondFragment(ImageGridFragment.class, mBundle, null);
       }else if(v.getId() == mBtn_BitMap.getId()){
           Bundle mBundle = new Bundle();
           ((HomePageActivity) mContext).addSecondFragment(BitmapMemeryFragment.class, mBundle, null);
       }else if(v.getId() == mBtn_Contact.getId()){
           Bundle mBundle = new Bundle();
           ((HomePageActivity) mContext).addSecondFragment(ContactFragment.class, mBundle, null);
       }
    }
}
