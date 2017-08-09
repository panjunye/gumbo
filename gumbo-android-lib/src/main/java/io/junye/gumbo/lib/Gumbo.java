package io.junye.gumbo.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class Gumbo {
    
    public static final String TAG = Gumbo.class.getSimpleName();

    static final String SP_DOWNLOAD_APK_ID = "io.junye.gumbo.lib.sp.DownloadApkId";

    private Context context;

    private String appName;

    private String baseUrl;

    private String appKey;

    private boolean deltaOn;

    private UpdateInfo updateInfo;

    private Gumbo(Context context) {
        this.context = context;
    }

    void checkUpdate(@NonNull final UpdateListener updateListener) {

        // 通知UI正在获取更新信息
        CheckUpdateTask task = new CheckUpdateTask(buildUpdateUrl());

        task.setResponseListener(new BaseAsyncTask.ResponseListener<UpdateInfo>() {
            @Override
            public void onFinish(UpdateInfo info) {

                Log.d(TAG, "完成检测更新");

                if (info != null) {

                    if (info.isUpdate()) {
                        Log.d(TAG, "需要更新");
                        updateInfo = info;

                        if(ApkUtils.getDownloadedApkPath(context, updateInfo) != null){
                            info.setDownloaded(true);
                        }

                        info.setTitle(buildTitle(info));

                        updateListener.onUpdate(Gumbo.this,info);

                    }else{
                        Log.d(TAG, "已经是最新版本");

                        updateListener.onLatest();
                    }
                } else {
                    Log.d(TAG, "检测更新失败");

                    updateListener.onFailed();
                }
            }
        });

        task.execute();
    }

    private String buildTitle(UpdateInfo info){

        StringBuilder titleBuilder = new StringBuilder();

        titleBuilder
                .append(appName)
                .append(":")
                .append(info.getVersionName())
                .append("(");

        if(info.isDownloaded()){
            titleBuilder.append("已下载)");
        }else{
            if(info.isDelta()){
                //增量更新
                titleBuilder.append("增量包").append(getMbSize(info.getPatchSize()));
            }else{
                // 全量更新
                titleBuilder.append("安装包").append(getMbSize(info.getTargetSize()));
            }
            titleBuilder.append("MB)");
        }

        return titleBuilder.toString();
    }

    private static double getMbSize(double size){
        return Math.round(size / 1024 / 1024 * 100) / 100;
    }

    private String buildUpdateUrl(){
        return baseUrl + "api/checkupdate/?appKey=" + appKey + "&versionCode=" + ApkUtils.getCurrentVersion(context);
    }

    /**
     * 用户决定安装APK
     */
    public void install(){

        if(updateInfo == null){
            return;
        }

        // 判断安装文件是否存在
        String apkPath = ApkUtils.getDownloadedApkPath(context, updateInfo);

        if(apkPath != null){

            Log.d(TAG,"APK已下载，直接安装");

            ApkUtils.installApk(context,apkPath);

        }else{

            Toast.makeText(context,"准备下载",Toast.LENGTH_SHORT).show();

            Log.d(TAG,"APK还没有下载,准备下载");

            if(deltaOn){
                updateInfo.setDelta(deltaOn);
            }

            ApkUtils.download(context, updateInfo, buildNotification());
        }

    }

    private String buildNotification(){
        return appName + updateInfo.getVersionName();
    }

    public static class Builder{

        private Context context;

        private String appName;

        private String baseUrl;

        private String appKey;

        private boolean deltaOn;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Gumbo create(){

            if(null == baseUrl || null == appKey){
                throw new RuntimeException("必须设置UpdateUrl和AppKey");
            }

            if(appName == null){
                appName = "新版本";
            }

            Gumbo gumbo = new Gumbo(context);

            gumbo.appName = appName;

            gumbo.baseUrl = baseUrl;

            gumbo.appKey = appKey;

            gumbo.deltaOn = deltaOn;

            return gumbo;

        }

        public Builder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setAppKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public Builder setDeltaOn(boolean deltaOn) {
            this.deltaOn = deltaOn;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public String getAppName() {
            return appName;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getAppKey() {
            return appKey;
        }

        public boolean isDeltaOn() {
            return deltaOn;
        }

    }

}
