package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.music.entity.SongListVO;
import com.example.music.entity.SongVo;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_detail);

        handler = new Handler();
        show = new Runnable() {
            @Override
            public void run() {
                //????????????
                //notifyDataSetChanged() ?????????????????????????????????????????????????????????Activity?????????Activity???????????????????????????????????????
                songListDetailAdapter.notifyDataSetChanged();
            }
        };

        songListID = getIntent().getStringExtra("songListID");
        songListVO = (SongListVO) getIntent().getSerializableExtra("songListVOS");

        System.out.println("??????Intent?????????????????????" + songListVO);

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
        iscolloct();
        initPopwindow(); //??????

        songListDetailAdapter = new SongListDetailAdapter(this,R.layout.detail_song_item,songVos);
        songListView.setAdapter(songListDetailAdapter);
        songListView.setOnItemClickListener(this);
        songListDetailAdapter.setInnerItemOnclickListener(this);
    }

    private void initPopwindow() {
        songpopupView = LayoutInflater.from(this).inflate(R.layout.songlist_detail_popup_layout, null);
        songpopupWindow = new PopupWindow(songpopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        songpopupWindow.setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));  //?????????????????????
        songpopupWindow.setFocusable(true); //????????????????????????
        songpopupWindow.setTouchable(true); //????????????????????????
        songpopupWindow.setOutsideTouchable(true); //??????popupWindow???????????????????????????????????????
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
        //???????????????
        TextView next_play_btn_area = songpopupView.findViewById(R.id.next_play_btn_area);
        next_play_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //???????????????
        TextView pop_colloct_btn_area = songpopupView.findViewById(R.id.pop_colloct_btn_area);
        pop_colloct_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songpopupWindow.dismiss();
                setBackgroundAlpha(0.7f);
                showAddSongPopwindow();
            }
        });
        //????????????
        TextView pop_share_btn_area = songpopupView.findViewById(R.id.pop_share_btn_area);
        pop_share_btn_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //????????????


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

                songVos.addAll(song);

                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });


    }

    //??????????????????
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra("songID",songVos.get(position).getId());
        intent.putExtra("songName",songVos.get(position).getName());
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

    //??????????????????
    private void showPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_song_list_detail,null);
        TextView song_name = songpopupView.findViewById(R.id.pop_song_title);
        song_name.setText(songVos.get(currentPosition).getAr().get(0).getName()+" - "+songVos.get(currentPosition).getName());
        songpopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }
}