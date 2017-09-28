package com.bolo1.beijinwisdom.activity.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.bolo1.beijinwisdom.activity.base.BasePager;

/**
 * Created by 菠萝 on 2017/9/27.
 */

public class SettringPager extends BasePager {
    public SettringPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView= new  TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);
        tv_bar_title.setText("设置");
    }
}
