package com.bolo1.beijinwisdom.activity.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.base.BasePager;

/**
 * Created by 菠萝 on 2017/9/27.
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }
    //初始化数据
    @Override
    public void initData() {
        TextView textView= new  TextView(mActivity);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);
        //改变title文字
        tv_bar_title.setText("首页");

    }
}