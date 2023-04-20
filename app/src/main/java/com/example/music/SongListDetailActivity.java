package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.adapter.SongListDetailAdapter;
import com.example.music.entity.Song;
import com.example.music.entity.SongListVO;
import com.example.music.entity.SongVo;
import com.example.music.singleton.PlayingSongList;
import com.example.music.util.URL2BitmapUtil;
import com.music.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SongListDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SongListDetailAdapter.InnerItemOnclickListener {

    private String songListID;
    private SongListVO songListVO;
    private ListView songListView;
    private List<SongVo> songVos = new ArrayList<>();
    private SongListDetailAdapter songListDetailAdapter;
    private Handler handler;
    private Runnable show;
    private View songpopupView;
    private int currentPosition;
    private PopupWindow songpopupWindow;
    private List<Song> songList = new ArrayList<>();
    private PlayingSongList playingSongList = PlayingSongList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_detail);

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        handler = new Handler();
        show = new Runnable() {
            @Override
            public void run() {
                //更新界面
                //notifyDataSetChanged() 可以在适配器绑定的数组后，不用重新刷新Activity，通知Activity的适配器更新列表的数据即可
                songListDetailAdapter.notifyDataSetChanged();
            }
        };

        songListID = getIntent().getStringExtra("songListID");
        songListVO = (SongListVO) getIntent().getSerializableExtra("songListVOS");

        ImageView songlist_cover = findViewById(R.id.songlist_cover);
        TextView songlist_name = findViewById(R.id.songlist_name);
        TextView user_name = findViewById(R.id.user_name);
        songListView = findViewById(R.id.songlist_detail_list_view);

        ImageView comment_btn = findViewById(R.id.comment_btn);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongListDetailActivity.this,SongListCommentActivity.class);
                intent.putExtra("songListID",songListID);
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        });

        try {
            songlist_cover.setImageBitmap(URL2BitmapUtil.getBitmap(songListVO.getCoverImgUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        songlist_name.setText(songListVO.getName());
        user_name.setText(songListVO.getCreator().getNickname());

        getData();
        iscolloct();
        initPopwindow(); //弹窗

        songListDetailAdapter = new SongListDetailAdapter(this,R.layout.detail_song_item,songVos);
        songListView.setAdapter(songListDetailAdapter);
        songListView.setOnItemClickListener(this);
        songListDetailAdapter.setInnerItemOnclickListener(this);
    }

    private void initPopwindow() {
        songpopupView = LayoutInflater.from(this).inflate(R.layout.songlist_detail_popup_layout, null);
        songpopupWindow = new PopupWindow(songpopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        songpopupWindow.setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));  //设置为透明背景
        songpopupWindow.setFocusable(true); //设置是否可以聚焦
        songpopupWindow.setTouchable(true); //设置是否可以触摸
        songpopupWindow.setOutsideTouchable(true); //设置popupWindow以外的区域可以响应触摸事件
        songpopupWindow.setAnimationStyle(R.style.animTranslate);
        songpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out = songpopupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songpopupWindow.dismiss();
            }
        });
        //下一首播放
        TextView next_play_btn_area = songpopupView.findViewById(R.id.next_play_btn_area);
        next_play_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //收藏到歌单
        TextView pop_colloct_btn_area = songpopupView.findViewById(R.id.pop_colloct_btn_area);
        pop_colloct_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songpopupWindow.dismiss();
                setBackgroundAlpha(0.7f);
                showAddSongPopwindow();
            }
        });
        //分享歌曲
        TextView pop_share_btn_area = songpopupView.findViewById(R.id.pop_share_btn_area);
        pop_share_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //删除歌曲


    }

    private void showAddSongPopwindow() {
    }

    private void iscolloct() {
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
                songVos.clear();

                String songs = JSONObject.parseObject(response.body().string()).get("songs").toString();
                List<SongVo> song= JSON.parseArray(songs, SongVo.class);
                for (int i=0;i<song.size();i++){
                    Song s = new Song();
                    s.setSongID(song.get(i).getId());
                    s.setSongName(song.get(i).getName());
                    s.setSingerID(song.get(i).getAr().get(0).getId());
                    s.setSingerName(song.get(i).getAr().get(0).getName());
                    s.setSongImage(song.get(i).getAl().getPicUrl());
                    songList.add(s);
                }
                songVos.addAll(song);

                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });

    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        playingSongList.InitPlayingSongList(songList,songVos.get(position).getId(),position);
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra("songID",songVos.get(position).getId());
        intent.putExtra("songName",songVos.get(position).getName());
        intent.putExtra("songer",songVos.get(position).getAr().get(0).getName());
        intent.putExtra("option","");
        startActivity(intent);
        overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }

    @Override
    public void itemClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()){
            case R.id.song_operate:
                currentPosition = position;
                showPopWindow();
                setBackgroundAlpha(0.7f);
        }
    }

    //显示底部弹窗
    private void showPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_song_list_detail,null);
        TextView song_name = songpopupView.findViewById(R.id.pop_song_title);
        song_name.setText(songVos.get(currentPosition).getAr().get(0).getName()+" - "+songVos.get(currentPosition).getName());
        songpopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }
}