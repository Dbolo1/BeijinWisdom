package com.bolo1.beijinwisdom.activity.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by 菠萝 on 2017/9/27.
 */

public class BasePager {
    public Activity mActivity;
    public ImageButton im_left_menu;
    public FrameLayout fl_content;
    public ImageView im_photos;
    public TextView tv_bar_title;
    public final View mRootView;


    public BasePager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.basepage, null);
        im_left_menu = (ImageButton) view.findViewById(R.id.im_left_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        tv_bar_title = (TextView) view.findViewById(R.id.tv_bar_title);
        im_photos= (ImageView) view.findViewById(R.id.im_photos);
        im_left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }
    public void initData(){


    }
    /**
     * 关闭侧滑栏
     */
    private void toggle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();

    }

}
