package com.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.MainActivity;
import com.bolo1.beijinwisdom.activity.base.BasePager;
import com.bolo1.beijinwisdom.activity.base.impl.GovAffairsPager;
import com.bolo1.beijinwisdom.activity.base.impl.HomePager;
import com.bolo1.beijinwisdom.activity.base.impl.NewsPager;
import com.bolo1.beijinwisdom.activity.base.impl.SettringPager;
import com.bolo1.beijinwisdom.activity.base.impl.SmartService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/27.
 */

/**
 * 主页面的fragment
 */
public class ContentFragment extends BaseFragment {

    private ViewPager vp_content_pager;
    private ArrayList<BasePager> mbasePagers;
    private RadioGroup rg_grop;
    private static final  String tag="ContentFragment";



    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content_pager = (ViewPager) view.findViewById(R.id.vp_content_pager);

        rg_grop = (RadioGroup) view.findViewById(R.id.rg_grop);
        return view;
    }

    @Override
    protected void initData() {
        //向里面填充fragment
        mbasePagers = new ArrayList<>();

        mbasePagers.add(new HomePager(mActivity));
        mbasePagers.add(new NewsPager(mActivity));
        mbasePagers.add(new SmartService(mActivity));
        mbasePagers.add(new GovAffairsPager(mActivity));
        mbasePagers.add(new SettringPager(mActivity));

        rg_grop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content_pager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp_content_pager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        vp_content_pager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        vp_content_pager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        vp_content_pager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });
        vp_content_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                BasePager basePager = mbasePagers.get(position);
                basePager.initData();
                Log.d(tag, "instantiateItem: 当前的position "+position);
                if (position == 0 || position == mbasePagers.size()-1) {
                    //如果是第一个和最后一个禁用侧滑菜单与隐藏菜单按钮
                    basePager.im_left_menu.setVisibility(View.GONE);
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                    basePager.im_left_menu.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_content_pager.setAdapter(new ContentAdapter());
        mbasePagers.get(0).initData();
    }



    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mbasePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = mbasePagers.get(position);
            View view = basePager.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    /**
     * @param enable 设置是否禁用策划面版
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingmenu = mainUI.getSlidingMenu();
        if (enable) {
//            slidingmenu.removeIgnoredView(vp_content_pager);
            Log.d(tag, "setSlidingMenuEnable:  全屏触摸"+ enable );
            slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {

            Log.d(tag, "setSlidingMenuEnable:  禁止触摸"+ enable );
//            slidingmenu.addIgnoredView(vp_content_pager);
            slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
     public NewsPager getNewsPager(){
        NewsPager  pager= (NewsPager) mbasePagers.get(1);
         return pager;
     }
}
