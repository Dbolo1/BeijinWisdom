package com.bolo1.beijinwisdom.activity.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.MainActivity;
import com.bolo1.beijinwisdom.activity.base.BaseDetailMenuPager;
import com.bolo1.beijinwisdom.activity.domain.NewsMenu;
import com.fragment.ContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/28.
 */

public class NewsMenuDetailPager extends BaseDetailMenuPager implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager viewPager;
    @ViewInject(R.id.topPanel_title)
    private TabPageIndicator mTabPageIndicator;
    private ArrayList<NewsMenu.NewsTabData> newsTabDatas;
    private ArrayList<TabDetailPager> mPagers;


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        newsTabDatas = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < newsTabDatas.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, newsTabDatas.get(i));
            mPagers.add(pager);
        }
        viewPager.setAdapter(new NewsMenuDetailAdapter());
        mTabPageIndicator.setViewPager(viewPager);
        mTabPageIndicator.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //监听当前选择页面位置
        if (position == 0) {
            setSlidingMenuEnable(true);
        } else {
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData newsTabData = newsTabDatas.get(position);
            return newsTabData.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view = pager.rootView;
            container.addView(view);
            pager.initData();

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * @param enable 设置侧边栏是否开启
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingmenu = mainUI.getSlidingMenu();
        if (enable) {
//            slidingmenu.removeIgnoredView(vp_content_pager);
            slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
//            slidingmenu.addIgnoredView(vp_content_pager);
            slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    @OnClick(R.id.imb_next_select)
    public void nextPage(View view) {
        //获取当前的位置
        int currentItem = viewPager.getCurrentItem();
        currentItem++;
        viewPager.setCurrentItem(currentItem);
    }
}
