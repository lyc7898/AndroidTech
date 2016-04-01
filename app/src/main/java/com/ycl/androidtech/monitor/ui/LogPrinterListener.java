package com.ycl.androidtech.monitor.ui;

/**
 * Created by yuchengluo on 2016/4/1.
 */
public interface LogPrinterListener {
    void onWaringLevel1(String loginfo,long time);//一组警告
    void onWaringLevel2(String loginfo,long time);//二组警告，更严重，可能需dump更多信息
}
