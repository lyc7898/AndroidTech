package com.android.androidtech.fragment.performance.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.androidtech.R;
import com.android.androidtech.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuchengluo on 2015/8/5.
 */
public class ListViewFragment extends BaseFragment {

    private List<HashMap<String, Object>> mData;
    private ListView listView;
    private Context mContext;
    boolean bool = false;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getHostActivity();
        View view = inflater.inflate(R.layout.fm_listview, container, false);
        mData = getData();//为刚才的变量赋值
        MyAdapter adapter = new MyAdapter(mContext);//创建一个适配器

        listView = (ListView) view.findViewById(R.id.listView);//实例化ListView
        listView.setAdapter(adapter);//为ListView控件绑定适配器
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

    /**
     * 自定义适配器
     */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;// 动态布局映射

        private class ItemHolder {
            TextView title;
            TextView time;
            TextView info;
            ImageView img;
        }
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // 决定ListView有几行可见
        @Override
        public int getCount() {
            return mData.size();// ListView的条目数
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder itemHolder = null;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.item_listview_test, null);//根据布局文件实例化view
                itemHolder = new ItemHolder();
                itemHolder.title = (TextView) convertView.findViewById(R.id.title);//找某个控件
                itemHolder.time = (TextView) convertView.findViewById(R.id.time);//找某个控件
                itemHolder.info = (TextView) convertView.findViewById(R.id.info);
                itemHolder.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(itemHolder);
            }else{
                itemHolder = (ItemHolder)convertView.getTag();
            }
            itemHolder.info.setText(mData.get(position).get("info").toString());
            itemHolder.time.setText(mData.get(position).get("time").toString());//给该控件设置数据(数据从集合类中来)
            itemHolder.title.setText(mData.get(position).get("title").toString());//给该控件设置数据(数据从集合类中来)
            itemHolder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
            return convertView;
        }
    }

    // 初始化一个List
    private List<HashMap<String, Object>> getData() {
        // 新建一个集合类，用于存放多条数据
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        for (int i = 1; i <= 40; i++) {
            map = new HashMap<String, Object>();
            map.put("title", "姓名" + i);
            map.put("time", "08-05");
            map.put("info", "我通过了你的好友验证请求");
            map.put("img", R.mipmap.ic_launcher);
            list.add(map);
        }

        return list;
    }

    public void showInfo(int position) {
        getData();
    }

}
