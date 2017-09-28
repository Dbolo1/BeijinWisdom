package com.bolo1.beijinwisdom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.bolo1.beijinwisdom.activity.util.Sputil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 菠萝 on 2017/9/20.
 */

public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private Button bt_start;
    private LinearLayout ll_container;
    private int[] mImageIds = new int[]{
           R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3,
    };
    private ArrayList<ImageView> mImageViews= new ArrayList<>();
    private ImageView im_move_point;
    private static final String tag="GuideActivity";
    private int mPointMarginLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initUI();
        initData();

    }

    private void initData() {
        initPoint();

    }

    private void initPoint() {
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViews.add(imageView);
            //初始化小点
            ImageView pointView = new ImageView(this);
            pointView.setImageResource(R.drawable.shape_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                //给小点加距离
                layoutParams.leftMargin = 20;
            }
            ll_container.addView(pointView, layoutParams);
        }
        //设置小红点跟随屏幕移动而移动
        im_move_point.setImageResource(R.drawable.shape_red_point);

        im_move_point.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除事件的监听
                im_move_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //在此监听画面是否测量结束

                mPointMarginLeft = ll_container.getChildAt(1).getLeft()-ll_container.getChildAt(0).getLeft();
                Log.d(tag,"小点的间距===="+ mPointMarginLeft);


            }
        });

        //监听
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时候调用
                Log.d(tag,"当前所在位置"+position+"移动的屏幕百分比:"+positionOffset);
                //设置小红点跟随界面同步移动
                int pointLeft  = (int) (mPointMarginLeft*positionOffset)+position*mPointMarginLeft;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) im_move_point.getLayoutParams();
                params.leftMargin=pointLeft;
                im_move_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                //页面选择时候调用
                //当导航界面到最后一页时显示按钮
                if(position==mImageViews.size()-1){
                    bt_start.setVisibility(View.VISIBLE);
                    bt_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Sputil.putBoolean(getApplicationContext(), ConstantValue.IS_FIRST_START,false);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    });
                }else {
                    bt_start.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态改变时候调用
            }
        });
        vp_guide.setAdapter(new MyAdapter());



    }


    private void initUI() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        bt_start = (Button) findViewById(R.id.bt_start);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        im_move_point = (ImageView) findViewById(R.id.im_move_point);
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mImageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
