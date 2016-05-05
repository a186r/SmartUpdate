package com.durs.smartupdate;

import android.os.Environment;

import java.io.File;

/**
 * Created by durs on 2016/5/5.
 */
public class Constants {
    public static final boolean DEBUG = true;

    //    //旧版本 正确的MD5值，如果本地安装的apk MD5值不是TA，说明本地安装的是被二次打包的apk
//    public static final String REAL_OLD_MD5 = "7dc46bd75c1042f943942a37d646afaa";
//
//    //新版本 正确的MD5值
//    public static final String REAL_NEW_MD5 = "c2da8edf4b796c1a393be38730ac93fe";
//
//    //用于测试的packageName
    public static final String TEST_PACKAGENAME = "com.lb.duoduo";

    public static final String PATH = Environment.getExternalStorageDirectory() + File.separator;

    //合成得到的新版apk
    public static final String NEW_APK_PATH = PATH + "smartUpdateFinal.apk";

    //final版本,,不需要拿到这个版本,只需要服务器下发MD5
    public static final String FINAL_APK_PATH = PATH + "final.apk";

    //从服务器下载来的差分包
    public static final String PATCH_PATH = PATH + "patchPackage.patch";
}
