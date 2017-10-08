package com.bolo1.beijinwisdom.activity.util;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by 菠萝 on 2017/10/8.
 */

public class MyBitmapUtils {

    private final NetCacheUtils netCacheUtils;

    public MyBitmapUtils(Activity mActivity){

        netCacheUtils = new NetCacheUtils();

    }
    public void display(ImageView imageView, String uri){
        netCacheUtils.getBitmapFromNet(imageView,uri);
    }

}
