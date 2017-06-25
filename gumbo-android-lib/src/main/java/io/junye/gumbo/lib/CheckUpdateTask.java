package io.junye.gumbo.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

class CheckUpdateTask extends BaseAsyncTask<String,Void,UpdateInfo> {


    private String mUpdateUrl;

    CheckUpdateTask(String updateUrl) {
        this.mUpdateUrl = updateUrl;
    }

    @Override
    protected UpdateInfo doInBackground(String... params) {
        return getUpdateInfo(mUpdateUrl);
    }

    private UpdateInfo getUpdateInfo(String updateUrl){
        HttpURLConnection urlConnection;
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[128];
        UpdateInfo info = null;
        try {
            urlConnection = (HttpURLConnection) new URL(updateUrl).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            int num;
            while((num = reader.read(buffer,0,buffer.length)) != -1){
                sb.append(buffer,0,num);
            }
            String jsonData = sb.toString();
            info = JsonUtil.read(UpdateInfo.class,jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

}