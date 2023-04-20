package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.adapter.SongListCommentAdapter;
import com.example.music.entity.Song;
import com.example.music.entity.SongListCommentVO;
import com.example.music.entity.SongVo;
import com.music.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongListCommentActivity extends AppCompatActivity {

    private List<SongListCommentVO> songListCommentVOS = new ArrayList<>();
    private String total;
    private Runnable show;
    private Handler handler;
    private SongListCommentAdapter songListCommentAdapter;
    private ListView songlist_comment_list_view;
    private String songListID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_comment);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }

        songListID = getIntent().getStringExtra("songListID");

        TextView commentCount = findViewById(R.id.commentText);
        songlist_comment_list_view = findViewById(R.id.songlist_comment_list_view);

        handler = new Handler();
        show = new Runnable() {
            @Override
            public void run() {
                //更新界面
                commentCount.setText(String.valueOf("评论(" + total + "条)"));
                songListCommentAdapter.notifyDataSetChanged();
            }
        };

        getData();

        songListCommentAdapter = new SongListCommentAdapter(this,songListCommentVOS);
        songlist_comment_list_view.setAdapter(songListCommentAdapter);
    }

    private void getData() {
        String url = "https://service-3jnzriln-1258693374.cd.apigw.tencentcs.com/comment/playlist?id="+ songListID;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                songListCommentVOS.clear();

                JSONObject body = JSONObject.parseObject(response.body().string());
                total = body.get("total").toString();
                String hotComments = body.get("comments").toString();
                songListCommentVOS.addAll(JSON.parseArray(hotComments,SongListCommentVO.class));

                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }
}