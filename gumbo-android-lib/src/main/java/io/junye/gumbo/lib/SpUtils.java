package io.junye.gumbo.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class SpUtils {

    private SharedPreferences sp;
    private static SpUtils instance;

    private SpUtils(Context context) {
        sp = context.getSharedPreferences("download_sp", Context.MODE_PRIVATE);
    }

    public static synchronized SpUtils get(Context context) {
        if (instance == null) {
            instance = new SpUtils(context.getApplicationContext());
        }
        return instance;
    }

    public SpUtils putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int dValue) {
        return sp.getInt(key, dValue);
    }

    SpUtils putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
        return this;
    }

    long getLong(String key, Long dValue) {
        return sp.getLong(key, dValue);
    }

    public SpUtils putFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
        return this;
    }

    public Float getFloat(String key, Float dValue) {
        return sp.getFloat(key, dValue);
    }

    public SpUtils putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
        return this;
    }

    public Boolean getBoolean(String key, boolean dValue) {
        return sp.getBoolean(key, dValue);
    }

    public SpUtils putString(String key, String value) {
        sp.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String dValue) {
        return sp.getString(key, dValue);
    }

    boolean putObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String str = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
        sp.edit().putString(key,str).apply();
        return true;
    }

    Object getObject(String key){

        String str = sp.getString(key,null);
        if(str == null)
            return null;

        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str,Base64.DEFAULT));

        ObjectInputStream ois;
        try{
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void remove(String key) {
        if (isExist(key)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    private boolean isExist(String key) {
        return sp.contains(key);
    }
}