package com.example.music.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URL2BitmapUtil {

    //URL图片转换为BitMap图片
    public static Bitmap getBitmap(String url) throws IOException {
        Bitmap bitmap = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL myUrl = new URL(url);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.setConnectTimeout(6000); //设置超时
            connection.setDoInput(true); //设置为true就可以调用getOutputStream()方法从服务器端获得字节输出流
            connection.setUseCaches(false); //不缓存
            //connection.connect();
            is = connection.getInputStream(); //获得图片的数据流
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is!=null){
                is.close();
            }
            if (connection!=null){
                connection.disconnect();
            }

        }
        return bitmap;
    }

}
