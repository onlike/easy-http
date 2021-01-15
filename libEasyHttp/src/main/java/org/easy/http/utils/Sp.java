package org.easy.http.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.easy.http.config.EasyOptions;

public class Sp {
    private static final String COOKIE_FILE = "cookieFileSp";

    /**
     * 写入本地
     */
    public static void write(String key, String value) {
        Context context = EasyOptions.get().getContext();
        if (context == null) return;
        SharedPreferences sp = context.getSharedPreferences(COOKIE_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 读取数据
     */
    public static String read(String key) {
        Context context = EasyOptions.get().getContext();
        if (context == null) return null;
        SharedPreferences sp = context.getSharedPreferences(COOKIE_FILE, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }
}
