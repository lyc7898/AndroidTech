package com.android.androidtech.fragment.performance.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.android.androidtech.R;
import com.android.androidtech.fragment.base.BaseFragment;
import com.android.androidtech.ui.MultiCardsView;
import com.android.androidtech.utils.GLog;

/**
 * Created by yuchengluo on 2015/7/16.
 * 过度绘制DEMO-实现一个没有过度绘制的自定义View
 */
public class OverDrawFragment extends BaseFragment {
    private int cardResId[] = {R.mipmap.test3, R.mipmap.test3,
            R.mipmap.test3, R.mipmap.test3};
    private MultiCardsView multicardsView = null;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_overdraw, container, false);
        multicardsView = (MultiCardsView) view.findViewById(R.id.cardview);
        multicardsView.enableOverdrawOpt(true);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int cardWidth = width /2;
        int cardHeight = height /2;
        int yOffset = 40;
        int xOffset = 40;
        for (int i = 0; i < cardResId.length; i++) {
            SingleCard cd = new SingleCard(new RectF(xOffset, yOffset, xOffset + cardWidth, yOffset + cardHeight));
            Bitmap bitmap = loadImageResource(cardResId[i], cardWidth, cardHeight);
            cd.setBitmap(bitmap);
            multicardsView.addCards(cd);
            xOffset += cardWidth / 3;
        }
        Button overdraw = (Button) view.findViewById(R.id.btn_overdraw);
        overdraw.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            multicardsView.enableOverdrawOpt(false);
                                        }
                                    }
        );
        Button perfectdraw = (Button) view.findViewById(R.id.btn_perfectdraw);
        perfectdraw.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            multicardsView.enableOverdrawOpt(true);
                                        }
                                    }
        );
        return view;
    }

    public Bitmap loadImageResource(int imageResId, int cardWidth, int cardHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inScreenDensity = DisplayMetrics.DENSITY_DEFAULT;
            BitmapFactory.decodeResource(getResources(), imageResId, options);

            // ����decode
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight, cardWidth, cardHeight);
            bitmap = BitmapFactory.decodeResource(getResources(), imageResId, options);
        } catch (OutOfMemoryError exception) {

            GLog.d("img", "loadImageResource : out of memory resid: " + imageResId);
        }
        return bitmap;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
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
    PowerManager.WakeLock mWakeLock = null;
    private void acquireWakeLock(Context ctx)
    {
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager)ctx.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "TestLocknService");
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
            }
        }
    }
    //释放设备电源锁
    private void releaseWakeLock()
    {
        if (null != mWakeLock)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
