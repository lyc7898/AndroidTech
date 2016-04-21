package com.ycl.androidtech.activity.ex;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ycl.androidtech.R;
import com.ycl.androidtech.monitor.memory.TestDataModel;

public class LayoutPerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_layout_per);
        //make a leak object
        TextView test = (TextView)this.findViewById(R.id.layout_per_txt_1);
        TestDataModel.getInstance().setRetainedTextView(test);
    }

}
