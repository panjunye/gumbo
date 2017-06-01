package io.junye.gumbo.lib.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import io.junye.gumbo.lib.Gumbo;
import io.junye.gumbo.lib.UpdateInfo;


/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class ApkUtils {

    private static final String DEBUG_TAG = "ApkUtils";

    public static void installApk(Context context, Uri uri) {

        Intent install = new Intent(Intent.ACTION_VIEW);
        if (uri != null)
        {
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        } else
        {
            Log.e(DEBUG_TAG, "Apk not exists");
        }
    }

    public static int getVersionCode(Context context){
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.d(DEBUG_TAG,"current versionCode:" + pInfo.versionCode);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean download(Context context,UpdateInfo info,boolean allowDelta,String title){

        if(info == null || !info.isUpdate()){
            return false;
        }

        String downloadUrl;

        if(info.isDelta() && allowDelta){
            downloadUrl = info.getPatchUrl();
        }else{
            downloadUrl = info.getApkUrl();
        }

        final String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,fileName)
                .setTitle(title)
                .setDescription(info.getUpdateLog())
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        long id =  dManager.enqueue(request);

        // 存储id
        SpUtils.get(context).putLong(Gumbo.SP_DOWNLOAD_APK_ID,id);

        SpUtils.get(context).putObject(Gumbo.SP_UPDATE_INFO,info);

        return true;
    }

    public static Uri getDownloadedApk(Context context,UpdateInfo newInfo){

        UpdateInfo oldInfo = (UpdateInfo) SpUtils.get(context).getObject(Gumbo.SP_UPDATE_INFO);
        if(oldInfo == null || !oldInfo.equals(newInfo) || oldInfo.getApkPath() == null){
            return null;
        }
        return Uri.fromFile(new File(oldInfo.getApkPath()));
    }
}
