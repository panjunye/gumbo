package io.junye.gumbo.lib;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

class ApkUtils {

    public static final String TAG = ApkUtils.class.getSimpleName();

    private static final String KEY = ApkUtils.class.getSimpleName();

    static void installApk(Context context, String apkPath) {

//
        if(apkPath == null){
            Log.e(KEY, "Apk not exists");
            return;
        }


        Log.d(TAG, "realPath: " + apkPath);

        File apkFile = new File(apkPath);

        openFile(apkFile,context);

    }

    private static void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        }else{
            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }


    static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    static int getCurrentVersion(Context context){
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Log.d(KEY,"current versionCode:" + pInfo.versionCode);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    static boolean download(Context context, UpdateInfo info, String title){

        if(info == null || !info.isUpdate()){
            return false;
        }

        String downloadUrl;

        if(info.isDelta()){
            downloadUrl = info.getPatchUrl();
        }else{
            downloadUrl = info.getApkUrl();
        }

        final String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);

        Uri uri = Uri.parse(downloadUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,fileName);

        request
                .setTitle(title)
                .setDescription(info.getUpdateLog())
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // TODO 如果是下载增量更新包，不应该显示notification

        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        long id =  dManager.enqueue(request);

        // 存储id
        SpUtils.get(context).putLong(Gumbo.SP_DOWNLOAD_APK_ID,id);

        SpUtils.get(context).putObject(UpdateInfo.KEY,info);

        return true;
    }

    static String getDownloadedApk(Context context, UpdateInfo newInfo){

        UpdateInfo oldInfo = (UpdateInfo) SpUtils.get(context).getObject(UpdateInfo.KEY);

        if(oldInfo == null || !oldInfo.equals(newInfo) || oldInfo.getApkPath() == null){
            return null;
        }

        // TODO 应当判断oldInfo.getApkPath的文件是否存在
        return oldInfo.getApkPath();
    }
}
