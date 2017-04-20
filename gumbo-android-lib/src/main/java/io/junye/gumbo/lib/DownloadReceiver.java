package io.junye.gumbo.lib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import io.junye.gumbo.lib.util.ApkUtils;
import io.junye.gumbo.lib.util.SpUtils;


/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class DownloadReceiver extends BroadcastReceiver{

    public static final String DEBUG_TAG = "DownloadReceiver";

    @Override
    public void onReceive(final Context context, Intent intent)
    {

        Log.d(DEBUG_TAG,"download complete");

        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);

            long id = SpUtils.get(context).getLong(Gumbo.SP_DOWNLOAD_APK_ID,-1L);



            if(downloadApkId == id) {
                DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri fileUri =  dManager.getUriForDownloadedFile(downloadApkId);

                if(fileUri == null){
                    return;
                }

                UpdateInfo info = (UpdateInfo) SpUtils.get(context).getObject(Gumbo.SP_UPDATE_INFO);
                if(info != null){
                    if(! info.isDelta()){
                        info.setApkPath(fileUri.getPath());
                        SpUtils.get(context).putObject(Gumbo.SP_UPDATE_INFO,info);
                        ApkUtils.installApk(context,fileUri);
                    }else{

                        // TODO 应当判断ExternalStorage是否可用
                        final File newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),info.getVersionName() + ".apk");
                        final String oldPath = context.getPackageResourcePath();

                        Intent serviceIntent = new Intent(context,BspatchService.class);
                        serviceIntent.putExtra(BspatchService.PATCH_PATH,fileUri.getPath());
                        serviceIntent.putExtra(BspatchService.NEW_PATH,newFile.getPath());
                        serviceIntent.putExtra(BspatchService.OLD_PATH,oldPath);

                        context.startService(serviceIntent);
                    }
                }
            }
        }
    }
}
