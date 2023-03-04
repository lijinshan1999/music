package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.adapter.SongListDetailAdapter;
import com.example.music.entity.SongListVO;
import com.example.music.entity.SongVo;
import com.example.music.util.URL2BitmapUtil;
import com.music.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SongListDetailActivity extends AppCompatActivity {

    private String songListID;
    private SongListVO songListVO;
    private ListView songListView;
    private List<SongVo> songVos = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_detail);


        songListID = getIntent().getStringExtra("songListID");
        songListVO = (SongListVO) getIntent().getSerializableExtra("songListVOS");

        System.out.println(songListVO);




        ImageView songlist_cover = findViewById(R.id.songlist_cover);
        TextView songlist_name = findViewById(R.id.songlist_name);
        TextView user_name = findViewById(R.id.user_name);
        songListView = findViewById(R.id.songlist_detail_list_view);

        try {
            songlist_cover.setImageBitmap(URL2BitmapUtil.getBitmap(songListVO.getCoverImgUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        songlist_name.setText(songListVO.getName());
        user_name.setText(songListVO.getCreator().getNickname());

        getData();


        System.out.println("-----------------------------------------------------------------------" + songVos);
        // SongListDetailAdapter songListDetailAdapter = new SongListDetailAdapter(this,R.layout.detail_song_item,songVos);
        //songListView.setAdapter(songListDetailAdapter);
    }

    public void getData() {
        String url = "https://service-3jnzriln-1258693374.cd.apigw.tencentcs.com/playlist/track/all?id=" + songListVO.getId() + "&limit=10&offset=1";

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String songs = JSONObject.parseObject(response.body().string()).get("songs").toString();
                songVos = JSON.parseArray(songs, SongVo.class);
                System.out.println(songVos);

                //String playlists = JSONObject.parseObject(response.body().string()).get("playlist").toString();
                //songListVOS = JSON.parseArray(playlists, SongListVO.class);

                //System.out.println(playlists);
                // System.out.println(songListVOS.toString());

            }
        });
    }


}