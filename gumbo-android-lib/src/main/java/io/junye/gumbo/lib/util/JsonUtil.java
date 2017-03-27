package io.junye.gumbo.lib.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * Created by Junye on 2017/3/23 0023.
 *
 */

public class JsonUtil {
    public static <T> T read(Class<T> clazz,String  jsonData){
        T t = null;
        try {
            JSONObject jobject = new JSONObject(jsonData);
            Constructor<T> c = clazz.getConstructor();
            c.setAccessible(true);
            t = c.newInstance();
            Iterator<String>  keys = jobject.keys();
            while (keys.hasNext()){
                String key = keys.next();
                try {
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(t,jobject.get(key));
                } catch (NoSuchFieldException | JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | JSONException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
