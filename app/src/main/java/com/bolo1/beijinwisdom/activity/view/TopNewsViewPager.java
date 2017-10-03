package com.bolo1.beijinwisdom.activity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 菠萝 on 2017/10/3.
 */

public class TopNewsViewPager extends ViewPager {

    private int startx;
    private int starty;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = (int) ev.getX();
                starty = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endx = (int) ev.getX();
                int endy = (int) ev.getY();
                int dx = endx - startx;
                int dy = endy - starty;
                if (Math.abs(dy) < Math.abs(dx)) {
                    //左右滑动
                    //判断左右滑动方向
                    if(dx>0){
                        //向右滑动
                        if(getCurrentItem()==0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //向左滑动
                    int count=getAdapter().getCount();
                        if(getCurrentItem()==count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
