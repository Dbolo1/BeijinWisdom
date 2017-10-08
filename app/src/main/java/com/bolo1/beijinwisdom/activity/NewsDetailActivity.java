package com.bolo1.beijinwisdom.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bolo1.beijinwisdom.R;
import com.bolo1.beijinwisdom.activity.util.ConstantValue;
import com.bolo1.beijinwisdom.activity.util.Sputil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 菠萝 on 2017/10/6.
 */

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.wv_webview)
    private WebView webView;
    @ViewInject(R.id.im_left_menu)
    private ImageButton im_left_menu;
    @ViewInject(R.id.im_black)
    private ImageButton im_black;
    @ViewInject(R.id.im_right_textsize)
    private ImageButton im_right_textsize;
    @ViewInject(R.id.im_right_share)
    private ImageButton im_right_share;
    @ViewInject(R.id.ll_control)
    private LinearLayout ll_control;
    @ViewInject(R.id.pb_web_process)
    private ProgressBar pb_web_process;
    private static final String tag = "NewsDetailActivity";
    private int fontType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_detail);
        initView();
        initData();
    }

    private void initData() {
        im_right_textsize.setOnClickListener(this);
        im_right_share.setOnClickListener(this);
        im_black.setOnClickListener(this);
    }

    private void initView() {
        ViewUtils.inject(this);
        ll_control.setVisibility(View.VISIBLE);
        im_black.setVisibility(View.VISIBLE);
        im_left_menu.setVisibility(View.GONE);
        String mUrl = getIntent().getStringExtra("url");
        webView.loadUrl(mUrl);
        pb_web_process.setMax(100);
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);//缩放按钮
        webSetting.setUseWideViewPort(true);//双击缩放
        webSetting.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb_web_process.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
                Log.d(tag, "开始加网页");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(tag, "网页加载完成");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(tag, "跳转url" + request.toString());
                view.loadUrl(request.toString());
                return true;

            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
////            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //进度的加载
                pb_web_process.setProgress(newProgress);
                if (newProgress == 100) {
                    pb_web_process.setVisibility(View.INVISIBLE);
                }
                Log.d(tag, "当前进度" + newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_black:
                finish();
                overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
                break;
            case R.id.im_right_textsize:
                showChooseDialog();
                break;
            case R.id.im_right_share:
                showShare();
                break;
            default:
                break;
        }
    }

    private int TempFont;

    /**
     * 显示选择字体窗口
     */
    private void showChooseDialog() {

        String[] FontSize = new String[]{
                "超大字体", "大字体",
                " 正常字体 ", "小字体", " 超小字体 ",
        };
        int fontType = Sputil.getInt(this, ConstantValue.FONT_SIZE, 2);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.SETTING_TYPESIZE);
        builder.setSingleChoiceItems(FontSize, fontType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TempFont = which;
            }
        });


        builder.setNegativeButton(R.string.CANCLE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.CONFIRM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings webSetting = webView.getSettings();
//                SMALLEST(50),
//                        SMALLER(75),
//                        NORMAL(100),
//                        LARGER(150),
//                        LARGEST(200);
                //在这记录字体，并更改webview字体
                Sputil.putInt(getApplicationContext(), ConstantValue.FONT_SIZE, TempFont);
                switch (TempFont) {
                    case 0:
//                        webSetting.setTextSize(WebSettings.TextSize.LARGER);
                        webSetting.setTextZoom(200);
                        break;
                    case 1:
                        webSetting.setTextZoom(150);
                        break;
                    case 2:
                        webSetting.setTextZoom(100);
                        break;
                    case 3:
                        webSetting.setTextZoom(75);
                        break;
                    case 4:
                        webSetting.setTextZoom(50);
                        break;

                }
            }
        });
        builder.show();
    }

    private void showShare() {
        MobSDK.init(this,"2171fee0bbd51"," f725263557387a289b30c585077541ba");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
