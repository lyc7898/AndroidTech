
package com.android.androidtech.fragment.performance.memory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.androidtech.R;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.miniimageloader.MiniImageLoader;
import com.android.miniimageloader.config.BitmapConfig;

public class ImageGridFragment extends BaseFragment {
    private static final String TAG = "ImageGridFragment";
//    private ImageWorker mImageWorker;
    private ImageAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mImageWorker = new ImageFetcher(getActivity(), 50);

//        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();
//
//        //
//        cacheParams.setMemCacheSizePercent(0.25f);

//        mImageWorker.initImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list, container, false);
        ListView listView = (ListView) v.findViewById(R.id.image_list);
        mAdapter = new ImageAdapter(getHostActivity());
        listView.setAdapter(mAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    MiniImageLoader.getInstance().setPauseWork(true);
                } else {
                    MiniImageLoader.getInstance().setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        return v;
    }

    @Override
    protected void resume() {
        mAdapter.notifyDataSetChanged();
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
    public void onDestroy() {
        super.onDestroy();
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

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return Images.imageThumbUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                imageView = (ImageView) convertView.findViewById(R.id.img);
            } else {
                imageView = (ImageView) convertView.findViewById(R.id.img);
            }
            MiniImageLoader.getInstance().loadImage(Images.imageThumbUrls[position], imageView,new BitmapConfig(50,50));
            return convertView;
        }
    }
}