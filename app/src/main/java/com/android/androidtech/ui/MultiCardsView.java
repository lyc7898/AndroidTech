package com.android.androidtech.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.android.androidtech.fragment.performance.ui.SingleCard;
import com.android.androidtech.utils.GLog;

import java.util.ArrayList;

/**
 * Created by yuchengluo on 2015/7/16.
 */
public class MultiCardsView extends View{
    private ArrayList<SingleCard> cardsList = new ArrayList<SingleCard>(5);
    private boolean enableOverdrawOpt = true;

    public MultiCardsView(Context context) {
        this(context, null, 0);
    }
    public MultiCardsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MultiCardsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addCards(SingleCard card) {
        cardsList.add(card);
    }
    //设置是否消除过度绘制
    public void enableOverdrawOpt(boolean enableOrNot) {
        this.enableOverdrawOpt = enableOrNot;
        invalidate();
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cardsList == null || canvas == null)
            return;
        Rect clip = canvas.getClipBounds();
        GLog.d("draw", String.format("clip bounds %d %d %d %d", clip.left, clip.top, clip.right, clip.bottom));
        //根据enableOverdrawOpt值来调用不同的绘制方法，对比效果
        if (enableOverdrawOpt) {
            drawCardsWithotOverDraw(canvas, cardsList.size() - 1);
        } else {
            drawCardsNormal(canvas, cardsList.size() - 1);
        }
    }
    //没有过度绘制的实现
    protected void drawCardsWithotOverDraw(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size())
            return;
        SingleCard card = cardsList.get(index);
        //判断是否没和某个卡片相交，从而跳过那些非矩形区域内的绘制操作
        if (card != null && !canvas.quickReject(card.area, Canvas.EdgeType.BW)) {
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            //只绘制可见区域
            if (canvas.clipRect(card.area, Region.Op.DIFFERENCE)) {
                drawCardsWithotOverDraw(canvas, index - 1);
            }
            canvas.restoreToCount(saveCount);
            saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            //只绘制可见区域
            if (canvas.clipRect(card.area)) {
                GLog.d("draw", "overdraw opt: draw cards index: " + index);
                Rect clip = canvas.getClipBounds();
                GLog.d("draw", String.format("current clip bounds %d %d %d %d", clip.left, clip.top, clip.right, clip.bottom));
                card.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }else{
            drawCardsWithotOverDraw(canvas, index - 1);
        }
    }
    //普通绘制
    protected void drawCardsNormal(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size())
            return;
        SingleCard card = cardsList.get(index);
        if (card != null) {
            drawCardsNormal(canvas, index - 1);
            GLog.d("draw", "draw cards index: " + index);
            card.draw(canvas);
        }
    }
}
