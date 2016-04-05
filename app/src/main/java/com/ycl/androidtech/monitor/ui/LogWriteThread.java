package com.ycl.androidtech.monitor.ui;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.ycl.androidtech.utils.GLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

/**
 * Created by yuchengluo on 2015/4/5.
 * 写日志线程
 */
public class LogWriteThread implements UiPerfMonitorConfig{
    private Handler mWriteHandler = null;
    private final Object FILE_LOCK = new Object();
    private final SimpleDateFormat FILE_NAME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String TAG = "LogWriteThread";
    public void saveLog(final String loginfo) {
        getmControlHandler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (FILE_LOCK) {
                    saveLog2Local(loginfo);
                }
            }
        });
    }
    private void saveLog2Local(String info){
        long time = System.currentTimeMillis();
        File logFile = new File(LOG_PATH + "/" + FILENAME + "-" + FILE_NAME_FORMATTER.format(time) + ".txt");
        StringBuffer mSb = new StringBuffer("/***************************************/\r\n");
        mSb.append(TIME_FORMATTER.format(time));
        mSb.append("\r\n/***************************************/\r\n");
        mSb.append(info+ "\r\n");
        GLog.d(TAG, "saveLogToSDCard: "+ mSb.toString());
        if(logFile.exists()){
            writeLog4SameFile(logFile.getPath(),mSb.toString());
        }else{
            BufferedWriter writer = null;
            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(logFile.getPath(), true), "UTF-8");
                writer = new BufferedWriter(out);
                writer.write(mSb.toString());
                writer.flush();
                writer.close();
                writer = null;
            } catch (Throwable t) {
                Log.e(TAG, "saveLogToSDCard: ", t);
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                        writer = null;
                    }
                } catch (Exception e) {
                    GLog.e(TAG, "saveLogToSDCard: ", e);
                }
            }
        }
    }
    /**
     * 追加内容：使用RandomAccessFile
     *
     * @param fileName
     *            文件名
     * @param content
     *            追加的内容
     */
    public static void writeLog4SameFile(String fileName, String content) {
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(randomFile != null){
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void send2Server(){
        getmControlHandler().post(new Runnable() {
            @Override
            public void run() {
                //TODO 上传到服务器
            }
        });
    }
    private Handler getmControlHandler() {
        if (null == mWriteHandler) {
            HandlerThread mHT = new HandlerThread("SamplerThread");
            mHT.start();
            mWriteHandler = new Handler(mHT.getLooper());
        }
        return mWriteHandler;
    }
}
