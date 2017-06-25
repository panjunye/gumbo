package io.junye.gumbo.lib;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;


/**
 * Created by Junye on 2017/3/27 0027.
 *
 */

public class BspatchService extends IntentService {

    public static final String TAG = "BspatchService";

    public static final String PATCH_PATH = "io.junye.bspatch.IntentService.PatchPath";

    public static final String NEW_PATH = "io.junye.bspatch.IntentService.NewPath";

    public static final String OLD_PATH = "io.junye.bspatch.IntentService.OldPath";

    public BspatchService() {
        super("BspatchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();

        String patchPath = intent.getStringExtra(PATCH_PATH);

        String newPath = intent.getStringExtra(NEW_PATH);

        String oldPath = intent.getStringExtra(OLD_PATH);


        if(patchPath == null || newPath == null || oldPath == null)
            return;

        int result = Bspatch.patch(oldPath,newPath,patchPath);

        if(result == -1){
            // patch合成失败，应该清空下载信息UpdateInfo
            SpUtils.get(context).remove(UpdateInfo.KEY);
            return;
        }


        UpdateInfo info = (UpdateInfo) SpUtils.get(context).getObject(UpdateInfo.KEY);

        info.setApkPath(newPath);

        SpUtils.get(context).putObject(UpdateInfo.KEY,info);

        PackageManager packManager = context.getPackageManager();

        PackageInfo packageInfo = packManager.getPackageArchiveInfo(newPath,0);

        // TODO 根据UpdateInfo校验新APK的MD5
        
        if(packageInfo != null
                && info.getVersionName().equals(packageInfo.versionName)
                && info.getVersionCode() == packageInfo.versionCode) {
            Log.d(TAG,"install apk");
            ApkUtils.installApk(context,newPath);
        }
    }

}
