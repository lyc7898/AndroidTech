package com.android.androidtech.fragment.performance.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.androidtech.GmfApplication;
import com.android.androidtech.R;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.utils.GLog;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuchengluo on 2015/11/17.
 */
public class BitmapMemeryFragment extends BaseFragment {

    ImageView mImageview = null;
    TextView mTexBm = null;
    private final String TAG = "BitmapMemeryFragment";
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_bitmap_show, container, false);
        mImageview = (ImageView) v.findViewById(R.id.viewstub_demo_imageview);
        mTexBm = (TextView) v.findViewById(R.id.text_bm_mem);
        showBitMap();
       // testMemeny();
        return v;
    }
    Bitmap[] mBitmap = null;
    private void testMemeny() {
        mBitmap = new Bitmap[10];
        new Thread() {
            public void run() {
                InputStream in = null;
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(2000);

                        in = GmfApplication.getContext().getResources().getAssets().open("jpg_1920_1080.jpg");
                        BitmapFactory.Options opts = getSampledBitmapOptionsFromStream(in, 1080, 1080);

                        mBitmap[i] = BitmapFactory.decodeStream(in, null, opts);
                        GLog.d(TAG,"testMemeny:" + i + "||size:" +  mBitmap[i].getByteCount());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showBitMap() {
        InputStream in = null;
        try {
            in = GmfApplication.getContext().getResources().getAssets().open("jpg_1920_1080.jpg");
            if (null == in) {
                return;
            }
            BitmapFactory.Options opts = getSampledBitmapOptionsFromStream(in, 1080, 1080);
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, opts);
            // Bitmap bitmap = BitmapFactory.decodeStream(in);
            if (null != bitmap) {
                mImageview.setImageBitmap(bitmap);
                mTexBm.setText("Bitmap.565&inSam：" + bitmap.getByteCount());
                GLog.d(TAG, "||size:" + bitmap.getByteCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static BitmapFactory.Options getSampledBitmapOptionsFromStream(
            InputStream is, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // 计算缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * @param @param  options
     * @param @param  reqWidth
     * @param @param  reqHeight
     * @param @return
     * @return int 缩小的比例
     * @throws
     * @Description: 计算图片缩放比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
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
}
