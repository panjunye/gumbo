package gumbo.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import io.junye.gumbo.lib.Gumbo;
import io.junye.gumbo.lib.UpdateInfo;
import io.junye.gumbo.lib.UpdateListener;

/**
 * Created by junye on 25/06/2017.
 */

public class UpdateHelperImpl implements UpdateHelper{

    private static final String TAG  = UpdateHelperImpl.class.getSimpleName();

    private static final String KEY_HAS_CHEKCED = "KEY_HAS_CHEKCED";

    private static final int ONE_DAY = 3600*24;

    private Activity activity;
    private String updateUrl;
    private String appKey;

    public UpdateHelperImpl(Activity activity, String updateUrl, String key) {
        this.activity = activity;
        this.updateUrl = updateUrl;
        this.appKey = key;
    }

    @Override
    public void checkUpdate() {

        Gumbo.Builder builder = new Gumbo.Builder(activity.getApplicationContext());

        builder.setAppName(activity.getString(R.string.app_name))
                .setUpdateUrl(updateUrl)
                .setAppKey(appKey)
                .setDeltaOn(false)
                .setUpdateListener(new UpdateListener() {
                    @Override
                    public void onUpdate(final Gumbo gumbo,final UpdateInfo info) {

                        Log.d(TAG, "onUpdate: " + info.toString());

                        if(activity.isFinishing()){
                            return;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle(info.getTitle())
                                .setMessage(info.getUpdateLog())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // User confirm to install the new apk.
                                        gumbo.install();
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .show();
                    }

                    @Override
                    public void onLatest() {
                        Log.d(TAG, "onLatest: ");
                    }

                    @Override
                    public void onLoading() {
                        Log.d(TAG, "onLoading: ");
                    }

                    @Override
                    public void onFailed() {
                        Log.d(TAG, "onFailed: ");
                    }
                });

        builder.build().checkUpdate();


    }

}
