package com.bolo1.beijinwisdom.activity.domain;

import java.util.ArrayList;

/**
 * Created by 菠萝 on 2017/9/28.
 */

public class NewsMenu {
    public ArrayList<Integer> extend;
    public int retcode;
    public ArrayList<NewsMenuData> data;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }

    public class NewsMenuData {
        public int id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;


        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public class NewsTabData {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
