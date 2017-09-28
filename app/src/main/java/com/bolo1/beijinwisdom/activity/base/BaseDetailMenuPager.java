package com.bolo1.beijinwisdom.activity.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by 菠萝 on 2017/9/28.
 */

public abstract class BaseDetailMenuPager {
    public Activity mActivity;
    public final View rootView;

    public BaseDetailMenuPager(Activity activity) {
        mActivity = activity;
        rootView = initView();
    }

    public abstract View initView();

    public abstract void initData();

}
