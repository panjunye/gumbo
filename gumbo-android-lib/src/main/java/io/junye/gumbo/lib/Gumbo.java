package io.junye.gumbo.lib;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class Gumbo {
    
    public static final String TAG = Gumbo.class.getSimpleName();

    static final String SP_DOWNLOAD_APK_ID = "io.junye.gumbo.lib.sp.DownloadApkId";

    private Context context;

    private String appName;

    private String updateUrl;

    private String appKey;

    private boolean deltaOn;

    private UpdateListener updateListener;

    private UpdateInfo updateInfo;

    private Gumbo(Context context) {
        this.context = context;
    }

    public void checkUpdate() {
        // 通知正在获取更新信息
        notifyOnLoading();

        CheckUpdateTask task = new CheckUpdateTask(buildUpdateUrl());

        task.setResponseListener(new BaseAsyncTask.ResponseListener<UpdateInfo>() {
            @Override
            public void onFinish(UpdateInfo info) {

                Log.d(TAG, "onFinish: 检测更新完成");

                if (info != null) {

                    if (info.isUpdate()) {
                        Log.d(TAG, "onFinish: 需要更新");
                        updateInfo = info;
                        if(ApkUtils.getDownloadedApk(context, updateInfo) != null){
                            info.setDownloaded(true);
                        }
                        info.setTitle(buildTitle(info));
                        notifyOnUpdate(info);
                    }else{
                        Log.d(TAG, "onFinish: ");
                        notifyOnLatest();
                    }
                } else {
                    notifyOnFailed();
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
        return Math.round(size / 1024.0 / 1024.0 * 100) / 100.0;
    }


    private String buildUpdateUrl(){
        return updateUrl + "?appKey=" + appKey + "&versionCode=" + ApkUtils.getCurrentVersion(context);
    }

    /**
     * 用户决定安装APK
     */
    public void install(){

        if(updateInfo == null){
            return;
        }

        // 判断安装文件是否存在
        String apkPath = ApkUtils.getDownloadedApk(context, updateInfo);

        if(apkPath != null){

            Log.d(TAG,"APK已下载，直接安装");

            ApkUtils.installApk(context,apkPath);

        }else{

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

    private void notifyOnUpdate(UpdateInfo info) {
        if(updateListener != null){
            updateListener.onUpdate(this,info);
        }
    }

    private void notifyOnLatest() {
        if(updateListener != null){
            updateListener.onLatest();
        }
    }

    private void notifyOnFailed() {
        if(updateListener != null){
            updateListener.onFailed();
        }
    }

    private void notifyOnLoading() {
        if(updateListener != null){
            updateListener.onLoading();
        }
    }

    public static class Builder{

        private Context context;

        private String appName;

        private String updateUrl;

        private String appKey;

        private boolean deltaOn;

        private UpdateListener updateListener;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Gumbo build(){

            if(null == updateUrl || null == appKey){
                throw new RuntimeException("必须设置UpdateUrl和AppKey");
            }

            if(appName == null){
                appName = "新版本";
            }

            Gumbo gumbo = new Gumbo(context);

            gumbo.appName = appName;

            gumbo.updateUrl = updateUrl;

            gumbo.appKey = appKey;

            gumbo.deltaOn = deltaOn;

            gumbo.updateListener = updateListener;

            return gumbo;

        }

        public Builder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
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

        public Builder setUpdateListener(UpdateListener updateListener) {
            this.updateListener = updateListener;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public String getAppName() {
            return appName;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public String getAppKey() {
            return appKey;
        }

        public boolean isDeltaOn() {
            return deltaOn;
        }


        public UpdateListener getUpdateListener() {
            return updateListener;
        }
    }

}
