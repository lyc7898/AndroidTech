package com.android.androidtech.utils.dex;

/**
 * Created by yuchengluo on 2015/10/30.
 */
public class ClassLoaderUtil {

//    private static final String TAG = "ClassLoaderUtil";
//    public static final String LIBRARY_DEX_OUT_PATH = "/data/data/com.tencent.qqmusic/dexout/";
//    private static final String ODEX_FILE_NAME_Extensions = ".dex";
//
//    public static void resetOdexFileMd5(String dexFilePath){
//        String key = getKey(dexFilePath);
//        if(!TextUtils.isEmpty(key)){
//            MLog.e(TAG,"resetOdexFileMd5 dexFilePath = "+dexFilePath+",key = "+key);
//            QQPlayerPreferences.getInstance().saveOdexFileMd5(key,null);
//        }
//    }
//
//    private static String getKey(String dexFilePath){
//        if(!TextUtils.isEmpty(dexFilePath)){
//            return Math.abs(dexFilePath.hashCode())+"";
//        }
//
//        return null;
//    }
//
//    /**
//     *
//     * @Discription:循环遍历目录，找出所有的JAR包
//     * @param file
//     * @param files
//     */
//    private static final void loopFiles(File file, List<File> files) {
//        try {
//            if (file.isDirectory()) {
//                File[] tmps = file.listFiles();
//                for (File tmp : tmps) {
//                    loopFiles(tmp, files);
//                }
//            } else {
//                String absolutePath = file.getAbsolutePath();
//                if (absolutePath.endsWith(".apk") || absolutePath.endsWith(".jar")) {
//                    files.add(file);
//                }
//            }
//        } catch (Exception e) {
//            MLog.e(TAG, e);
//        }
//    }
//
//    private static String getMd5(String filePath){
//        String md5 = null;
//        File file = new File(filePath);
//        if(file.exists() && file.isFile()){
//            md5 = Util4File.getMD5EncryptedString(file);
//        }
//        return md5;
//    }
//
//
//
//    /**
//     *
//     * @Discription:加载JAR文件
//     * @param file
//     */
//    public static final void loadClassFromFile(File file) {
//        try {
//            if (null == file) {
//                MLog.e(TAG, "input file is null!");
//                return;
//            }
//
//
//            File dexOutDir = new File(LIBRARY_DEX_OUT_PATH);
//            if (!dexOutDir.exists()) {
//                dexOutDir.mkdirs();
//            }
//
//
//            Context context = MusicApplication.getContext();
//            if (null != context) {
//                ClassLoader systemLoaderClassLoader = context.getClassLoader();
//
//                String libPathsString = file.getPath();
//                String currentAppClassAPKs = MusicApplication.getContext().getPackageResourcePath();
//                MLog.d(TAG, "load libs currentAppClassAPKs:" + currentAppClassAPKs);
//                DexClassLoader loader = loadDexAndGetClassLoader(libPathsString, dexOutDir.getAbsolutePath(), null,
//                        systemLoaderClassLoader.getParent());
//
//                try {
//                    Field field = ClassLoader.class.getDeclaredField("parent");
//                    field.setAccessible(true);
//                    field.set(systemLoaderClassLoader, loader);
//                } catch (Exception e) {
//                    MLog.e(TAG, e);
//                }
//
//                MLog.d(TAG, "load libs:" + libPathsString);
//            } else {
//                MLog.e(TAG, "Context is null!");
//            }
//        } catch (Exception e) {
//            MLog.e(TAG, e);
//        }
//    }
//
//    /**
//     *
//     * @Discription:从一个目录加载所有JAR、APK文件
//     * @parampath
//     *            ：目录地址，如果送入一个JAR或者APK文件地址，则表示加载当前文件中的Class
//     */
//    public static final void loadClassFromDir(String dirPath) { //耗时居然在589ms
//        try {
//            MLog.d(TAG, "try to load libs from dir:" + dirPath);
//
//            if (TextUtils.isEmpty(dirPath)) {
//                MLog.e(TAG, "input dirPath is empty!");
//                return;
//            }
//
//            List<File> files = new ArrayList<File>();
//            File lib = new File(dirPath);
//            loopFiles(lib, files);
//            for (File file : files) {
//                loadClassFromFile(file);
//            }
//        } catch (Exception e) {
//            MLog.e(TAG, e);
//        }
//    }
//
//    /**
//     *
//     * @Discription:SDK是否可以使用
//     * @return
//     */
//    public static final boolean checkIfFordSDKInstalled() {
//        boolean isFordSDKLoaded = isFordSDKLoaded();
//        if (isFordSDKLoaded) {
//            return true;
//        } else {
//            try {
//                String sdkFilePath = LibraryDownloadManager.LIBRARY_LOCAL_INSTALL_PATH
//                        + LibraryDownloadManager.FORD_SDK_JAR_NAME;
//                QFile file = new QFile(sdkFilePath);
//                if (file != null && file.exists()) {
//                    MLog.e(TAG, "downloadFordSDK checkIfFordSDKInstalled() need to load");
//
//                    // SDK已经下载成功，但是未加载，则手工加载
//                    ClassLoaderUtil.loadClassFromDir(sdkFilePath);
//                    // 再次查询是否加载成功
//                    isFordSDKLoaded = isFordSDKLoaded();
//                    if (isFordSDKLoaded) {
//                        MLog.e(TAG, "downloadFordSDK checkIfFordSDKInstalled() need to load, and success");
//                        // 加载成功需要初始化福特的SDK
//                        // 初始化FordManager
//                        FordManager.getInstance().startProxy();
//                    }
//                    return isFordSDKLoaded;
//                }
//            } catch (Exception e) {
//                MLog.e(TAG, e);
//            } catch (Error e) {
//                MLog.e(TAG, e);
//            }
//        }
//        return false;
//    }
//
//    /**
//     *
//     * @Discription:SDK是否加载
//     * @return
//     */
//    public static final boolean isFordSDKLoaded() {
//        boolean isFordSDKLoaded = false;
//        try {
//            Class<?> fordClassObject = Class.forName("com.ford.syncV4.proxy.SyncProxyALM");
//            isFordSDKLoaded = null != fordClassObject;
//        } catch (Exception e) {
//            MLog.e(TAG, "ClassNotFoundException: Ford SDK is not loaded, need to download.");
//        } catch (Error e) {
//            MLog.e(TAG, "ClassNotFoundError: Ford SDK is not loaded, need to download.");
//        }
//
//        return isFordSDKLoaded;
//    }
//
//    public static void downloadFordSDK() {
//        try {
//            InstanceManager downloadManager = InstanceManager.getInstance(InstanceManager.INSTANCE_LIBRARY_DOWNLOAD);
//            if (null != downloadManager) {
//                ((LibraryDownloadManager) downloadManager).startDownload(LibraryDownloadManager.FORD_SDK_DOWNLOAD_URL,
//                        LibraryDownloadManager.FORD_SDK_JAR_NAME);
//            }
//        } catch (Exception e) {
//            MLog.e(TAG, e);
//        } catch (Error e) {
//            MLog.e(TAG, e);
//        }
//    }
//
//    public static void check4InitOrDownload() {
//        MLog.d(TAG, "downloadFordSDK check4InitOrDownload()");
//        if (ClassLoaderUtil.checkIfFordSDKInstalled()) {
//            FordManager.getInstance().startProxy();
//        } else {
//            MLog.d(TAG, "downloadFordSDK check4InitOrDownload() try to download");
//            // 启动下载
//            ClassLoaderUtil.downloadFordSDK();
//        }
//    }
//
//    public static DexClassLoader loadDexAndGetClassLoader(String dexFilePath,String odexDirPath,String solibDirPath,ClassLoader parent){
//
//        //1：先校验MD5
//        String odexMd5 = null;
//        String odexFilePath = null;
//        String key = null;
//        try {
//            MLog.e(TAG, "loadDexAndGetClassLoader dexFilePath = "+dexFilePath+",odexDirPath = "+odexDirPath+",solibDirPath = "+solibDirPath+",parent = "+parent.getClass().getName());
//            odexFilePath = odexDirPath;
//
//            //默认路径
//            if(TextUtils.isEmpty(odexFilePath)){
//                odexFilePath = LIBRARY_DEX_OUT_PATH;
//                odexDirPath = LIBRARY_DEX_OUT_PATH;
//            }
//
//            if (!odexFilePath.endsWith(File.separator)) {
//                odexFilePath += File.separator;
//            }
//
//            File dexFile = new File(dexFilePath);
//            String odexFileName = dexFile.getName().substring(0,dexFile.getName().lastIndexOf("."));
//            odexFileName += ODEX_FILE_NAME_Extensions;
//
//            odexFilePath += odexFileName;
//
//            MLog.e(TAG,"OdexFilePath = "+odexFilePath);
//            key = getKey(dexFilePath);
//            try {
//                odexMd5 = getMd5(odexFilePath);
//                String saveOdexMd5 = QQPlayerPreferences.getInstance().getOdexFileMd5(key);
//                MLog.e(TAG, "loadDexAndGetClassLoader odexMd5 = "+odexMd5+",saveOdexMd5 = "+saveOdexMd5);
//                if (TextUtils.isEmpty(saveOdexMd5) ||TextUtils.isEmpty(odexMd5) || !saveOdexMd5.equalsIgnoreCase(odexMd5)) {
//                    MLog.e(TAG, "loadDexAndGetClassLoader delete odex file and reset md5");
//                    //MD5对不上，需要两个操作，1：删除odex文件 2：重新计算MD5(假设认为dex是正确的)
//                    odexMd5 = null;
//
//                    File odexFile = new File(odexFilePath);
//                    odexFile.delete();
//                }
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//
//        }catch(Throwable e){
//            e.printStackTrace();
//        }
//
//
//        DexClassLoader loader = new DexClassLoader(dexFilePath, odexDirPath,
//                solibDirPath, parent);
//
//        GLog.e(TAG, "loadDexAndGetClassLoader DexClassLoader success");
//        try{
//            if(TextUtils.isEmpty(odexMd5)){
//                //没有MD5，重新算一遍
//                odexMd5 = getMd5(odexFilePath);
//                if(!TextUtils.isEmpty(odexMd5)){
//                    QQPlayerPreferences.getInstance().saveOdexFileMd5(key,odexMd5);
//                    MLog.e(TAG, "loadDexAndGetClassLoader save md5 odexMd5 = "+odexMd5+",key = "+key);
//                }
//
//            }
//        }catch(Throwable e){
//            e.printStackTrace();
//        }
//
//        return loader;
//    }

}
