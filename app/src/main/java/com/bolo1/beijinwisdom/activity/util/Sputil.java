package com.bolo1.beijinwisdom.activity.util;

/**
 * Created by 菠萝 on 2017/9/20.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于封装SharedPreferences
 */
public class Sputil {
    public static SharedPreferences sp;


    public static String getString(Context ctx, String key, String defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static void putString(Context ctx, String key, String defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, defValue).commit();
    }


    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, defValue).commit();
    }


    public static int getInt(Context ctx, String key, int defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    public static void putInt(Context ctx, String key, int defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, defValue).commit();
    }

    /**
     * 移除key
     *
     * @param ctx 上下文环境
     * @param key 键对
     */
    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }

}
