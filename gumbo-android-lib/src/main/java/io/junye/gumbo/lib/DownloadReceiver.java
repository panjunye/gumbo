package io.junye.gumbo.lib;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;


/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class DownloadReceiver extends BroadcastReceiver{

    public static final String TAG = "DownloadReceiver";

    @Override
    public void onReceive(final Context context, Intent intent)
    {

        Log.d(TAG,"download complete");

        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);

            long id = SpUtils.get(context).getLong(Gumbo.SP_DOWNLOAD_APK_ID,-1L);

            if(downloadApkId != -1L && downloadApkId == id) {

                DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri fileUri =  dManager.getUriForDownloadedFile(downloadApkId);

                if(fileUri == null){
                    Log.d(TAG, "fileUri: 不存在");
                    return;
                }

                UpdateInfo info = (UpdateInfo) SpUtils.get(context).getObject(UpdateInfo.KEY);

                if(info != null){
                    // TODO 由于用户设置deltaOn为false，导致即使info.isDelta为true，但是下载的文件仍然可能为apk文件
                    if(!info.isDelta()){
                        info.setApkPath(ApkUtils.getRealFilePath(context,fileUri));
                        SpUtils.get(context).putObject(UpdateInfo.KEY,info);
                        ApkUtils.installApk(context,info.getApkPath());
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
