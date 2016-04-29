package com.android.androidtech.utils.dex;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * Created by yuchengluo on 2015/10/30.
 */
public class DexUtil {
    public static boolean loadDexToClassLoader(String dexFilePath, String libPath, String optimizedDirectoryPath,
                                               PathClassLoader classLoader) {
        boolean result = false;
        if (TextUtils.isEmpty(dexFilePath) || classLoader == null) {
            return false;
        }

        try {
            List<File> additionalClassPathEntries = new ArrayList<File>();
            additionalClassPathEntries.add(new File(dexFilePath));
            MultiDex.installSecondaryDexes(classLoader, new File(optimizedDirectoryPath), additionalClassPathEntries);
            result = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
