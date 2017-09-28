package com.bolo1.beijinwisdom.activity.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bolo1.beijinwisdom.activity.base.BaseDetailMenuPager;

/**
 * Created by 菠萝 on 2017/9/28.
 */

public class NewsMenuDetailPager extends BaseDetailMenuPager {
    public NewsMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView= new  TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {

    }
}
