package com.durs.smartupdate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cundong.utils.PatchUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Button btn_smartupdate;
    private PackageInfo packageInfo;
    private String currentRealMD5, newRealMD5;
    private String oldVersionUrl;
    private int patchResult;

    private ProgressDialog mProgressDialog;

    //合并成功
    private static final int SUCCESS = 0;
    private File patchFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerListener();
    }

    private void registerListener() {
        btn_smartupdate.setOnClickListener(this);
    }

    private void initView() {
        mContext = getApplicationContext();
        btn_smartupdate = (Button) findViewById(R.id.btn_smartupdate);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在合并......");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_smartupdate:
                patchFile = new File(Constants.PATCH_PATH);
                if (null != patchFile) {
                    new PatchApkTask().execute();
                }
                break;
            default:
                break;
        }
    }

    private class PatchApkTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            packageInfo = ApkUtils.getInstalledApkPackageInfo(mContext, Constants.TEST_PACKAGENAME);
            if (null != packageInfo) {
                //向服务器请求当前安装版本MD5
//                requestMD5(packageInfo.versionCode, packageInfo.versionName);
                //获取已安装apk文件的路径,data/app
                oldVersionUrl = ApkUtils.getSourceApkPath(mContext, Constants.TEST_PACKAGENAME);
                if (!TextUtils.isEmpty(oldVersionUrl)) {
                    //判断当前安装的文件MD5是否和服务器返回的MD5相同
//                    if (SignUtils.checkMd5(oldVersionUrl, currentRealMD5)) {
                    //获取当前安装版本的MD5
                    Log.i("result", "当前安装版本MD5:" + SignUtils.getMd5ByFile(new File(oldVersionUrl)));
                    Log.i("result", "合成后文件的MD5:" + SignUtils.getMd5ByFile(new File(Constants.NEW_APK_PATH)));
                    Log.i("result", "实际发布的全新版本的MD5:" + SignUtils.getMd5ByFile(new File(Constants.FINAL_APK_PATH)));
                    //合并
                    patchResult = PatchUtils.patch(oldVersionUrl, Constants.NEW_APK_PATH, Constants.PATCH_PATH);
                    if (patchResult == 0) {//合并成功
                        Log.i("result", "到这里就合并成功了");
//                            if (SignUtils.checkMd5(Constants.NEW_APK_PATH, newRealMD5)) {
                        //合并成功那就安装合并之后的包
                        Log.i("result", "文件" + Constants.NEW_APK_PATH + "合并成功...");
                        ApkUtils.installApk(mContext, Constants.NEW_APK_PATH);
//                            } else {
//                                Log.i("result", "合并成功,但是合成之后的apkMD5错误...");
//                            }
                    } else {
                        Log.i("result", "合并失败...");
                    }
//                    } else {
//                        Log.i("result", "当前安装版本MD5有误,或许不是正版...");
//                    }

                } else {
                    Log.i("result", "无法获取当前安装apk路径,只能整包更新...");
                    //TODO 整包更新逻辑
                }
            } else {
                Log.i("result", "packageInfo为null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    /**
     * 应该从服务器请求,判断本地MD5跟服务器返回MD5是不是相等,防止被篡改
     *
     * @param versionCode
     * @param versionName
     */
    private void requestMD5(int versionCode, String versionName) {
//        currentRealMD5 = Constants.WEIBO_OLD_MD5;
//        newRealMD5 = Constants.WEIBO_NEW_MD5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    static {
        System.loadLibrary("ApkPatchLibrary");
    }
}
