package com.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.MainActivity;
import com.bolo1.beijinwisdom.activity.base.impl.NewsPager;
import com.bolo1.beijinwisdom.activity.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/27.
 */

public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_left_menu)
    private ListView listView;
    private int CurrentPos =0;
    private ArrayList<NewsMenu.NewsMenuData> newsMenuData;
    private LeftMenuAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.left_menu, null);
        ViewUtils.inject(this, view);
//        view.findViewById(R.id.lv_left_menu);
        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * @param data 获得json数据的data
     */
    public void setMenuData(final ArrayList<NewsMenu.NewsMenuData> data) {
        CurrentPos=0;
        newsMenuData = data;
        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentPos = position;
                adapter.notifyDataSetChanged();
                toggle();
                setCurrentDetailPager(position);
            }
        });
    }

    /**
     *
     * @param position 设置当前的详情页
     */
    private void setCurrentDetailPager(int position) {
        MainActivity  mainUI= (MainActivity) mActivity;
        ContentFragment   contentFragment=mainUI.getContentFragment();
        //获取新闻页面对象
        NewsPager pager=contentFragment.getNewsPager();
         pager.setCurrentDetailPager(position);

    }

    /**
     * 关闭侧滑栏
     */
    private void toggle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();

    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsMenuData.size();
        }

        @Override
        public Object getItem(int position) {
            return newsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.left_menu_item, null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_left_menu);
            if (position == CurrentPos) {
                tvMenu.setEnabled(true);
            } else {
                tvMenu.setEnabled(false);
            }
            NewsMenu.NewsMenuData newsMenuData = (NewsMenu.NewsMenuData) getItem(position);
            tvMenu.setText(newsMenuData.title);
            return view;
        }
    }
}
