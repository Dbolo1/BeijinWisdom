package com.bolo1.beijinwisdom.activity.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bolo1.beijinwisdom.activity.MainActivity;
import com.bolo1.beijinwisdom.activity.base.BaseDetailMenuPager;
import com.bolo1.beijinwisdom.activity.base.BasePager;
import com.bolo1.beijinwisdom.activity.base.menu.InteractMenuDetailPager;
import com.bolo1.beijinwisdom.activity.base.menu.NewsMenuDetailPager;
import com.bolo1.beijinwisdom.activity.base.menu.PhotosMenuDetailPager;
import com.bolo1.beijinwisdom.activity.base.menu.TopicMenuDetailPager;
import com.bolo1.beijinwisdom.activity.domain.NewsMenu;
import com.bolo1.beijinwisdom.activity.util.CacheUtil;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.fragment.LeftMenuFragment;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/27.
 */

public class NewsPager extends BasePager {

    private static final String tag = "NewsPager";
    private ArrayList<BaseDetailMenuPager> baseDetailMenuPagers;
    private NewsMenu newsData;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        fl_content.addView(textView);
        tv_bar_title.setText("新闻中心");


        String cache = CacheUtil.getCache(mActivity, ConstantValue.NEWS_URL);
        //判断是否有缓存
        if (!TextUtils.isEmpty(cache)) {
            //发现缓存
            processData(cache);
//          DataFromService();
        } else {
            //从服务器取得数据
            DataFromService();
        }
    }

    private void DataFromService() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantValue.NEWS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                //写入缓存
                CacheUtil.setCache(mActivity, ConstantValue.NEWS_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                Log.d(tag, s);
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        newsData = gson.fromJson(result, NewsMenu.class);
        //获得侧滑栏的对象
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenu = mainUi.getLeftMenuFragment();
        //向侧滑栏写入数据
        leftMenu.setMenuData(newsData.data);

        baseDetailMenuPagers = new ArrayList<BaseDetailMenuPager>();
        baseDetailMenuPagers.add(new NewsMenuDetailPager(mActivity));
        baseDetailMenuPagers.add(new TopicMenuDetailPager(mActivity));
        baseDetailMenuPagers.add(new PhotosMenuDetailPager(mActivity));
        baseDetailMenuPagers.add(new InteractMenuDetailPager(mActivity));

        //给新闻详情页设置默认页
        setCurrentDetailPager(0);
    }

    public void setCurrentDetailPager(int position) {

        BaseDetailMenuPager baseDetailMenuPager = baseDetailMenuPagers.get(position);
        View view = baseDetailMenuPager.rootView;
        fl_content.removeAllViews();
        fl_content.addView(view);
        baseDetailMenuPager.initData();


        tv_bar_title.setText(newsData.data.get(position).title);
    }


}
