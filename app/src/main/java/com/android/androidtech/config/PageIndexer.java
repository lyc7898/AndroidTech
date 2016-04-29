package com.android.androidtech.config;

/**
 * Created by yuchengluo on 2015/6/26.
 * 各页面的定义，如页面的ID
 */
public interface PageIndexer {

    //首页TAB ID
    public static final int TAB_VIEW_00 = 0;
    public static final int TAB_VIEW_01 = 1;
    public static final int TAB_VIEW_02 = 2;
    public static final int TAB_VIEW_03 = 3;
    /*
         * 首页三个页面index
         */
    public final static int APP_MAIN_1 = 1000;
    public final static int APP_MAIN_2 = 1001;
    public final static int APP_MAIN_3 = 1002;
    public final static int APP_MAIN_4 = 1003;

    //Activity跳转Bundle Key
    public final static String APP_INDEX_KEY = "app_index_key";
}
