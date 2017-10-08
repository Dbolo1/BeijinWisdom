package com.bolo1.beijinwisdom.activity.domain;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/10/8.
 */

public class PhotosBean {
    public PhotosData data;

    public class PhotosData{
         public String more;
        public ArrayList<PhotosNews> news;
    }

//    comment: true,
//    commentlist: "http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json",
//    commenturl: "http://zhbj.qianlong.com/client/user/newComment/82772",
//    id: 82772,
//    largeimage: "http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg",
//    listimage: "http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg",
//    pubdate: "2014-11-07 11:40",
//    smallimage: "http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg",
//    title: "北京·APEC绚丽之夜",
//    type: "news",
//    url: "http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html"
    public class PhotosNews{
        public int id;
        public String title;
        public String listimage;


    }

}
