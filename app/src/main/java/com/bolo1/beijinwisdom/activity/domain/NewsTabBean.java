package com.bolo1.beijinwisdom.activity.domain;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/10/3.
 */

public class NewsTabBean {

    public NewsTab data;

    public class NewsTab {
       public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNews> topnews;

    }

    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsData{" +
                    "id=" + id +
                    ", listimage='" + listimage + '\'' +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
    public class TopNews{
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNews{" +
                    "id=" + id +
                    ", topimage='" + topimage + '\'' +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
