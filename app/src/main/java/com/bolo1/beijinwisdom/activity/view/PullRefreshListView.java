package com.bolo1.beijinwisdom.activity.view;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bolo1.beijinwisdom.R;

import java.util.Date;

/**
 * Created by 菠萝 on 2017/10/4.
 */

public class PullRefreshListView extends ListView {
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELESEA_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private int mCurrentState;
    private View mHeadView;
    private int mHeadViewHeight;
    private int startY = -1;
    private ImageView iv_arrow_refresh;
    private ProgressBar pb_refresh;
    private TextView tv_refresh_state;
    private TextView tv_refresh_data;
    private RotateAnimation animationUp;
    private RotateAnimation animationDown;
    private int padding;


    public PullRefreshListView(Context context) {
        super(context);
        initHeadListView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadListView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadListView();
    }

    public void initHeadListView() {
        mHeadView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeadView);
        iv_arrow_refresh = (ImageView) mHeadView.findViewById(R.id.iv_arrow_refresh);
        pb_refresh = (ProgressBar) mHeadView.findViewById(R.id.pb_refresh);
        tv_refresh_state = (TextView) mHeadView.findViewById(R.id.tv_refresh_state);
        tv_refresh_data = (TextView) mHeadView.findViewById(R.id.tv_refresh_data);
        //隐藏头布局
        mHeadView.measure(0, 0);
        mHeadViewHeight = mHeadView.getMeasuredHeight();
        mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
        initAnimation();
    }
    //触摸判断

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //获取竖直方向开始移动坐标
                startY = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                //如果正在刷新则跳过
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                int endY = (int) ev.getY();
                int dy = endY - startY;
                int firstVisiblePosition = getFirstVisiblePosition();
                if (dy > 0 && firstVisiblePosition == 0) {
                    //拉出头listview
                    padding = dy - mHeadViewHeight;
                    mHeadView.setPadding(0, padding, 0, 0);
                    //判断当前刷新条状态
                    if (padding > 0 && mCurrentState != STATE_RELESEA_TO_REFRESH) {
                        //更改当前状态为释放刷新

                        mCurrentState = STATE_RELESEA_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {

                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (padding > 0 && mCurrentState != STATE_REFRESHING) {
                    mHeadView.setPadding(0, 0, 0, 0);
                    mCurrentState = STATE_REFRESHING;
                    //4.进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                    refreshState();
                } else if (padding < 0 && mCurrentState == STATE_PULL_TO_REFRESH) {
                    mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据刷新状态判断
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                //下拉刷新
                tv_refresh_state.setText(R.string.PULL_REFRESH);
                pb_refresh.setVisibility(INVISIBLE);
                iv_arrow_refresh.setVisibility(VISIBLE);
                iv_arrow_refresh.startAnimation(animationDown);

                break;
            case STATE_RELESEA_TO_REFRESH:
                //释放刷新
                tv_refresh_state.setText(R.string.RELESEA_REFRESH);
                pb_refresh.setVisibility(INVISIBLE);
                iv_arrow_refresh.setVisibility(VISIBLE);
                iv_arrow_refresh.startAnimation(animationUp);
                break;
            case STATE_REFRESHING:
                //刷新
                iv_arrow_refresh.clearAnimation();
                pb_refresh.setVisibility(VISIBLE);
                iv_arrow_refresh.setVisibility(INVISIBLE);
                tv_refresh_state.setText(R.string.REFRESHING);

                break;
        }
    }

    public void initAnimation() {
        animationUp = new RotateAnimation(
                0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        animationUp.setDuration(200);
        animationUp.setFillAfter(true);

        animationDown = new RotateAnimation(
                -180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        animationDown.setDuration(200);
        animationDown.setFillAfter(true);

    }

    //1 .回调接口刷新
    public interface OnRefreshListener {
        public void onRefresh();
    }

    //2.设置监听回调事件
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    //3 设置监听事件对象
    private OnRefreshListener mListener;

    //收起控件
    public void onRefreshComplete(boolean success) {
        mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
        tv_refresh_state.setText(R.string.PULL_REFRESH);
        pb_refresh.setVisibility(INVISIBLE);
        iv_arrow_refresh.setVisibility(VISIBLE);
        mCurrentState = STATE_PULL_TO_REFRESH;
        if (success) {
            getCurrentTime();
        }
    }

    //获取当前时间
    public void getCurrentTime() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        tv_refresh_data.setText(time);

    }
}
