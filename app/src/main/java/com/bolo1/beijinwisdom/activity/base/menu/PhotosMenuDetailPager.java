package com.bolo1.beijinwisdom.activity.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.base.BaseDetailMenuPager;
import com.bolo1.beijinwisdom.activity.domain.PhotosBean;
import com.bolo1.beijinwisdom.activity.util.CacheUtil;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.bolo1.beijinwisdom.activity.util.MyBitmapUtils;
import com.bolo1.beijinwisdom.activity.util.Sputil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/28.
 */

public class PhotosMenuDetailPager extends BaseDetailMenuPager implements View.OnClickListener {
    @ViewInject(R.id.lv_photos)
    private ListView lv_photos;
    @ViewInject(R.id.gv_photos)
    private GridView gv_photos;
    private ArrayList<PhotosBean.PhotosNews> mNewsList;
    private ImageView im_photos;

    public PhotosMenuDetailPager(Activity activity, ImageView im_photos) {
        super(activity);
        this.im_photos = im_photos;
        im_photos.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtil.getCache(mActivity, ConstantValue.PHOTOS_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpuitl = new HttpUtils();
        httpuitl.send(HttpRequest.HttpMethod.GET, ConstantValue.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtil.setCache(mActivity, ConstantValue.PHOTOS_URL, result);
                processData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosbean = gson.fromJson(result, PhotosBean.class);
        mNewsList = photosbean.data.news;
        lv_photos.setAdapter(new PhotosAdapter());
        gv_photos.setAdapter(new PhotosAdapter());

    }

    private boolean isListView = true;

    @Override
    public void onClick(View v) {
        if (isListView) {
            im_photos.setImageResource(R.drawable.icon_pic_list_type);
            lv_photos.setVisibility(View.GONE);
            gv_photos.setVisibility(View.VISIBLE);
            isListView = false;
        } else {
            im_photos.setImageResource(R.drawable.icon_pic_grid_type);
            lv_photos.setVisibility(View.VISIBLE);
            gv_photos.setVisibility(View.GONE);
            isListView = true;
        }
    }

    class PhotosAdapter extends BaseAdapter {
        private final MyBitmapUtils mBitmapUtils;

//        private final BitmapUtils mBitmapUtils;

        public PhotosAdapter() {
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils.configDefaultLoadFailedImage(R.drawable.pic_item_list_default);
            mBitmapUtils = new MyBitmapUtils(mActivity);

        }

        @Override
        public int getCount() {

            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.photos_menu, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.im_photos_image);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_photos_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PhotosBean.PhotosNews item = (PhotosBean.PhotosNews) getItem(position);
            viewHolder.title.setText(item.title);
            mBitmapUtils.display(viewHolder.imageView, item.listimage);

            return convertView;
        }
    }

    private class ViewHolder {
        TextView title;
        ImageView imageView;
    }
}
