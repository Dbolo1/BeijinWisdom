package com.bolo1.beijinwisdom.activity.base.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.NewsDetailActivity;
import com.bolo1.beijinwisdom.activity.base.BaseDetailMenuPager;
import com.bolo1.beijinwisdom.activity.domain.NewsMenu;
import com.bolo1.beijinwisdom.activity.domain.NewsTabBean;
import com.bolo1.beijinwisdom.activity.util.CacheUtil;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.bolo1.beijinwisdom.activity.util.Sputil;
import com.bolo1.beijinwisdom.activity.view.PullRefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/10/1.
 */

public class TabDetailPager extends BaseDetailMenuPager {
    private NewsMenu.NewsTabData mTabData;

    @ViewInject(R.id.vp_top_news)
    private ViewPager viewPager;
    @ViewInject(R.id.tv_topnews_title)
    private TextView textView;
    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;
    @ViewInject(R.id.lv_news)
    private PullRefreshListView listView;

    private String url;
    private ArrayList<NewsTabBean.TopNews> newsTabData;
    private ArrayList<NewsTabBean.NewsData> newsDatas;
    private String mMoreUrl;
    private static final String tag = "TabDetailPager";
    private NewsTabBean newsTabBean;
    private TabDetailPager.NewsAdapter NewsAdapter;
    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);
        View viewHeader = View.inflate(mActivity, R.layout.list_news_header, null);
        ViewUtils.inject(this, viewHeader);
        listView.addHeaderView(viewHeader);
        listView.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataServer();
            }

            @Override
            public void onLoadMore() {
                //加载更多
                Log.d(tag, "更多数据url" + mMoreUrl);
                if (mMoreUrl != null) {
                    getLoadMoreData();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    listView.onRefreshComplete(true);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取头布局数量
                int headerViewsCount = listView.getHeaderViewsCount();
                position = position - headerViewsCount;
                NewsTabBean.NewsData news = newsDatas.get(position);
                String readIds = Sputil.getString(mActivity, ConstantValue.READ_IDS, "");
                if (!readIds.contains(news.id + "")) {
                    //如果包含已存储的id
                    readIds = readIds + news.id + ",";
                    Sputil.putString(mActivity, ConstantValue.READ_IDS, readIds);
                }
                TextView textView = (TextView) view.findViewById(R.id.tv_news_title);
                textView.setTextColor(Color.GRAY);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            }
        });


        return view;
    }

    /**
     * 从服务器加载更多数据
     */
    private void getLoadMoreData() {
        HttpUtils httpUtil = new HttpUtils();
        httpUtil.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //解析数据
                String result = responseInfo.result;
//                CacheUtil.setCache(mActivity, url, result);
                processData(result, true);
                listView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
        url = ConstantValue.NEW_SERVICE_URL + mTabData.url;
        //获取数据
        String cache = CacheUtil.getCache(mActivity, url);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }
        getDataServer();
    }

    private void getDataServer() {
        HttpUtils httpUtil = new HttpUtils();
        httpUtil.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //解析数据
                String result = responseInfo.result;
                CacheUtil.setCache(mActivity, url, result);
                processData(result, false);
                listView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete(false);
            }
        });
    }

    private void processData(String result, boolean isMoreLoad) {

        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);
        String moreUrl = newsTabBean.data.more;
        Log.d(tag, "moreUrl " + moreUrl);
        //判断更多数据链接是否为空
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = ConstantValue.NEW_SERVICE_URL + moreUrl;
        } else {
            mMoreUrl = null;
        }

        if (!isMoreLoad) {
            newsTabData = newsTabBean.data.topnews;
            if (newsTabData != null) {
                viewPager.setAdapter(new PagerTabAdapter());
                indicator.setViewPager(viewPager);
                indicator.setSnap(true);
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        NewsTabBean.TopNews topNews = newsTabData.get(position);
                        textView.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                textView.setText(newsTabData.get(0).title);
                indicator.onPageSelected(0);
            }
            newsDatas = newsTabBean.data.news;
            if (newsDatas != null) {
                NewsAdapter = new NewsAdapter();
                listView.setAdapter(NewsAdapter);
            }
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //获取当前viewpager条目
                        int currentItem = viewPager.getCurrentItem();
                        currentItem++;
                        if(currentItem>newsTabData.size()-1){
                            currentItem=0;
                        }

                        viewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);
                viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                mHandler.sendEmptyMessageDelayed(0,3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0,3000);
                                break;
                        }

                        return false;
                    }
                });
            }
        } else {
            //加载更多数据
            ArrayList<NewsTabBean.NewsData> newsData = newsTabBean.data.news;
            newsDatas.addAll(newsData);
            NewsAdapter.notifyDataSetChanged();
        }
    }

    class PagerTabAdapter extends PagerAdapter {

        private final BitmapUtils bitmapUtils;

        public PagerTabAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return newsTabData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
//            imageView.setImageResource(R.drawable.topnews_item_default);
            //设置缩放模式填充父控件
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String topimageUrl = newsTabData.get(position).topimage;
            bitmapUtils.display(imageView, topimageUrl);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class NewsAdapter extends BaseAdapter {

        private final BitmapUtils bitmapUtils;

        public NewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return newsDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return newsDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                viewHolder = new ViewHolder();
                viewHolder.im_news_icon = (ImageView) convertView.findViewById(R.id.im_news_icon);
                viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData newsData = (NewsTabBean.NewsData) getItem(position);
            viewHolder.tv_news_title.setText(newsData.title);
            String readIds = Sputil.getString(mActivity, ConstantValue.READ_IDS, "");


            if (readIds.contains(newsData.id + "")) {
                viewHolder.tv_news_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_news_title.setTextColor(Color.BLACK);
            }

            viewHolder.tv_date.setText(newsData.pubdate);
            bitmapUtils.display(viewHolder.im_news_icon, newsData.listimage);
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView im_news_icon;
        private TextView tv_news_title;
        private TextView tv_date;

    }
}
