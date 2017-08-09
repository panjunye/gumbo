package io.junye.gumbo.lib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by junye on 08/08/2017.
 */

public class GumboInterface {

    public static final String TAG = GumboInterface.class.getSimpleName();

    private static final String META_DATA_KEY_APP_KEY = "GUMBO_APPKEY";

    private static final String META_DATA_KEY_BASE_URL = "GUMBO_URL";

    public static final String KEY_HAS_CHECKED_UPDATE = "KEY_HAS_CHECKED_UPDATE";

    private static boolean checking;

    private static Integer UPDATE_INTERVAL;

    /**
     * 检测更新的时间间隔
     */
    private static String APP_KEY;

    private static String BASE_URL;

    private static String APP_NAME;

    private static Boolean DELTA_ON = false;

    private static Context context;

    private static Gumbo gumbo;

    public static void setAppKey(String appKey){
        APP_KEY = appKey;
    }

    public static void setBaseUrl(String baseUrl){
        BASE_URL = baseUrl;
    }

    public static void setAppName(String appName){
        APP_NAME = appName;
    }

    public static void setDeltaOn(Boolean deltaOn) {
        DELTA_ON = deltaOn;
    }

    public static void setUpdateInterval(Integer updateInterval) {
        UPDATE_INTERVAL = updateInterval;
    }

    static Boolean getDeltaOn() {
        return DELTA_ON;
    }

    String getAppName(){
        return APP_NAME;
    }

    String getAppKey(){
        return APP_KEY;
    }

    String getBaseUrl(){
        return BASE_URL;
    }

    public static void init(Context context){

        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "Gumbo初始化失败: ");
            return;
        }

        Bundle bundle = ai.metaData;

        setAppKey(bundle.getString(META_DATA_KEY_APP_KEY));

        setBaseUrl(bundle.getString(META_DATA_KEY_BASE_URL));

        GumboInterface.context = context;

    }

    public static void checkUpdate(final UpdateListener updateListener,boolean force){

        if(!force && UPDATE_INTERVAL != null){
            if(ACache.get(context).getAsObject(KEY_HAS_CHECKED_UPDATE) != null){
                Log.d(TAG, "checkUpdate: 已经检测过更新,当前检测周期为" + UPDATE_INTERVAL + "秒");
                return;
            }
        }

        if(checking){
            Log.d(TAG, "checkUpdate: 正在检测更新");
            return;
        }
        checking = true;

        if(gumbo == null){
            createGumbo();
        }

        gumbo.checkUpdate(new UpdateListener() {
            @Override
            public void onUpdate(Gumbo gumbo, UpdateInfo info) {

                if(UPDATE_INTERVAL != null){
                    ACache.get(context).put(KEY_HAS_CHECKED_UPDATE,true,UPDATE_INTERVAL);
                }

                try{
                    updateListener.onUpdate(gumbo,info);
                }catch (Throwable ignored){}
                checking = false;
            }

            @Override
            public void onLatest() {
                try{
                    updateListener.onLatest();
                }catch (Throwable ignored){}
                checking = false;
            }

            @Override
            public void onFailed() {
                try{
                    updateListener.onFailed();
                }catch (Throwable ignored){}
                checking = false;
            }
        });
    }

    public static void checkUpdate(final UpdateListener updateListener){
        checkUpdate(updateListener,false);
    }

    private static void createGumbo() {

        Gumbo.Builder builder = new Gumbo.Builder(context);

        builder.setAppKey(APP_KEY)
                .setAppName(APP_NAME)
                .setDeltaOn(DELTA_ON)
                .setBaseUrl(BASE_URL);

        gumbo = builder.create();

    }
}
