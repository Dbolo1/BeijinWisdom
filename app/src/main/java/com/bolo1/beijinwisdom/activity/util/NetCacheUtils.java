package com.bolo1.beijinwisdom.activity.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 从网络获得储存
 */

class NetCacheUtils {

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            imageView.setTag(url);
//        }
//    };

    public void getBitmapFromNet(ImageView imageView, String uri) {
        new BitmapTask().execute(imageView, uri);
    }

    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {
        private ImageView imageView;
        private String url;


        //准备加载, 主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.print("onPreExecute");
        }

        //进行加载,运行在子线程
        @Override
        protected Bitmap doInBackground(Object... params) {
            System.out.print("doInBackground");
            imageView = (ImageView) params[0];
            url = (String) params[1];
//            mHandler.sendEmptyMessage(0);
            Bitmap bitmap = download(url);

            return bitmap;
        }

        /**
         * @param url 下载图片的url
         * @return Bitmap, 返回null为异常
         */
        private Bitmap download(String url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                int response = connection.getResponseCode();
                if (response == 200) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        //加载进度,主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            System.out.print("onProgressUpdate");
            super.onProgressUpdate(values);
        }

        //结束加载，主线程
        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.print("onPostExecute");
            if (result != null) {
//                String urlTag= (String) imageView.getTag();
//                if(urlTag.equals(url)){
                imageView.setImageBitmap(result);
//                }

            }
            super.onPostExecute(result);
        }
    }

}
