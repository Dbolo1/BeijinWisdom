package com.bolo1.beijinwisdom.activity;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.fragment.ContentFragment;
import com.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by 菠萝 on 2017/9/20.
 */

public class MainActivity extends SlidingFragmentActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用Material Design
        initUI();
        initData();
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), ConstantValue.TAG_LEFT_MENU);
        transaction.replace(R.id.fl_content_main, new ContentFragment(), ConstantValue.TAG_RIGHT_MENU);
        transaction.commit();

    }

    private void initData() {
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setBehindOffset(200);

    }

    private void initUI() {
        TextView tv_activity_bar = (TextView) findViewById(R.id.tv_activity_bar);
        TextView tv_bar_title = (TextView) findViewById(R.id.tv_bar_title);
        ImageView im_left_menu = (ImageView) findViewById(R.id.im_left_menu);

    }
    /**
     * 获取侧滑栏的对象
     */
    public LeftMenuFragment getLeftMenuFragment(){
       FragmentManager  fm= getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(ConstantValue.TAG_LEFT_MENU);
        return leftMenuFragment;
    }

    /**
     *
     * @return 主页面的fragment
     */
    public ContentFragment getContentFragment(){
        FragmentManager  fm= getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(ConstantValue.TAG_RIGHT_MENU);
        return contentFragment;
    }
}
