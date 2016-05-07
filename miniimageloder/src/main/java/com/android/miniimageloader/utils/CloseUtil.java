package com.android.miniimageloader.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by yuchengluo on 2016/5/7.
 */
public class CloseUtil {
    public static void closeQuietly(Closeable closeable){
        if(null != closeable){
            try{
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
