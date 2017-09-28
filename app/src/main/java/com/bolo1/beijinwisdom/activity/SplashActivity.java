package com.bolo1.beijinwisdom.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.MainActivity;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.bolo1.beijinwisdom.activity.util.Sputil;

import java.util.ArrayList;

public class SplashActivity extends Activity {

    private ImageView im_splash_view;
    private AnimationSet animationSet;
    private RelativeLayout rl_splash_root;
    private boolean is_first_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initAnimation();
        initData();
    }

    private void initData() {
        //在起始页面判断是否开启导航页面
        rl_splash_root.startAnimation(animationSet);
        is_first_start = Sputil.getBoolean(this, ConstantValue.IS_FIRST_START, true);
        //对动画集合监听 当动画结束时开启activity
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = null;
                if (is_first_start) {
                    //开启导航界面
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                } else {
                    //进入首页
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    /**
     * 设置动画 1 旋转 2 缩放 3 渐进
     */
    private void initAnimation() {
        //旋转
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(1000);

        //缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1, 0, 1,
                RotateAnimation.RELATIVE_TO_SELF,
                RotateAnimation.RELATIVE_TO_SELF
        );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐进
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //将动画添加到动画集合中
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
    }

    private void initUI() {
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);

    }
}
