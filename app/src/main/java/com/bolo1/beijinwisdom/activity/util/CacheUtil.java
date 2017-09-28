package com.bolo1.beijinwisdom.activity.util;

import android.content.Context;

import java.net.URL;

/**
 * 存入与读取缓存的util
 */

public class CacheUtil {
    /**
     *
     * @param ctx 上下文
     * @param url url地址
     * @param json 默认的json数据
     */
        public static void setCache(Context ctx,String url,String json){
           Sputil.putString(ctx,url,json);
        }

    /**
     *
     * @param ctx 上下文
     * @param url url地址
     *
     * @return  存储的缓存json数据
     */
        public static String getCache(Context ctx,String url){
           return Sputil.getString(ctx,url,null);
        }
}
