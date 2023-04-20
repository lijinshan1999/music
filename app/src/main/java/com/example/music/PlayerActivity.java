package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music.adapter.PlayListAdapter;
import com.example.music.entity.Song;
import com.example.music.service.MusicService;
import com.example.music.singleton.PlayingSongList;
import com.example.music.util.URL2BitmapUtil;
import com.music.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayListAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {

    private MyConnection connection;
    private Intent serviceIntent;
    private ImageView play_btn;
    private ImageView pre_btn;
    private ImageView next_btn;
    private ImageView playingList_btn;
    private MusicService.MyBinder musicControl;
    private SeekBar sb_progress;
    private static final int UPDATE_PROGRESS = 0;
    private ObjectAnimator objectAnimator;
    private View playlistPopupView;
    private boolean mode;
    private List<Song> playlist = new ArrayList<>();
    private ListView playlist_list_view;
    private PopupWindow playlistPopupWindow;
    private ImageView mode_image;
    private TextView play_mode_title;
    private ImageView clear_btn;
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private TextView title_text;
    private ImageView cover_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //更换顶部颜色
        //保证界面风格统一
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_player);
        String songID = getIntent().getStringExtra("songID");
        String songName = getIntent().getStringExtra("songName");
        String songer = getIntent().getStringExtra("songer");
        //System.out.println("songID:" + songID + "\nsongName:"+ songName + "\nsonger:"+ songer);
        //System.out.println(playingSongList.getList());

        title_text = findViewById(R.id.title_Text);
        cover_image = findViewById(R.id.cover_Image);

        objectAnimator = ObjectAnimator.ofFloat(cover_image, "rotation", 0f, 360f);
        objectAnimator.setDuration(50000); //设定持续时间
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1); //重复次数
        objectAnimator.start();

        sb_progress = findViewById(R.id.progress_sb);
        pre_btn = findViewById(R.id.pre_btn);
        play_btn = findViewById(R.id.play_btn);
        next_btn = findViewById(R.id.next_btn);
        playingList_btn = findViewById(R.id.playingList_btn);

        pre_btn.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        playingList_btn.setOnClickListener(this);

        //播放进度调正
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (progress > musicControl.getDuration() - 1){
                        progress = musicControl.getDuration() - 50;
                    }
                    musicControl.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //启动并绑定Service
        connection = new MyConnection();
        serviceIntent = new Intent(this, MusicService.class);

        startService(serviceIntent);
        bindService(serviceIntent,connection,BIND_AUTO_CREATE);
        playlist = playingSongList.getList();
        initPopwindow();

    }

    //播放列表操作弹窗
    private void initPopwindow() {
        playlistPopupView = LayoutInflater.from(this).inflate(R.layout.playlist_popup_layout, null, false);
        playlist_list_view = playlistPopupView.findViewById(R.id.playlist_list_view);
        playlistPopupView.findViewById(R.id.playlist_list_view);
        PlayListAdapter playListAdapter = new PlayListAdapter(this,playlist);
        playListAdapter.setOnInnerItemOnClickListener(this);
        playlist_list_view.setOnItemClickListener(this);
        playlist_list_view.setAdapter(playListAdapter);

        playlistPopupWindow = new PopupWindow(playlistPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        playlistPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        playlistPopupWindow.setFocusable(true);
        playlistPopupWindow.setTouchable(true);
        playlistPopupWindow.setOutsideTouchable(true);
        playlistPopupWindow.setAnimationStyle(R.style.animTranslate);
        playlistPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out = playlistPopupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistPopupWindow.dismiss();
            }
        });
        mode_image = playlistPopupView.findViewById(R.id.mode_image);
        mode_image.setOnClickListener(this);
        play_mode_title = playlistPopupView.findViewById(R.id.play_mode_title);
        clear_btn = playlistPopupView.findViewById(R.id.clear_image);
        clear_btn.setOnClickListener(this);
        play_mode_title.setOnClickListener(this);

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

    }

    @Override
    public void itemClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()){
            case R.id.delete_btn:
                Toast.makeText(this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class MyConnection implements ServiceConnection, MediaPlayer.OnCompletionListener{

        //当服务成功连接时调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MyBinder) service;
            musicControl.getPlayer().setOnCompletionListener(this);

            if (getIntent().getStringExtra("option").equals("next")){
                musicControl.newPlay();
            }else {
                //防止切出去再返回后重新播放
                if (!playingSongList.ifEqual()){
                    playingSongList.setindex();
                    musicControl.newPlay();
                }

            }


            updatePlayText();

            //设置进度条的最大值
            sb_progress.setMax(musicControl.getDuration());
            //设置进度条的进度
            sb_progress.setProgress(musicControl.getCurrentPosition());

            UpdateView();
            handler.sendEmptyMessage(UPDATE_PROGRESS);

        }


        //当与服务的连接断开时调用
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    }

    //更新当前界面的View 展示
    private void UpdateView() {
        title_text.setText(playingSongList.getNow().getSongName());
        try {
            cover_image.setImageBitmap(URL2BitmapUtil.getBitmap(playingSongList.getNow().getSongImage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //islikesong();
    }

    //根据播放状态调整播放/暂停按钮的显示
    private void updatePlayText() {

        if (musicControl.isPlaying()){
            play_btn.setImageResource(R.drawable.pause);
        } else {
            play_btn.setImageResource(R.drawable.player_play);
        }
    }

    //实时更新进度条
    private void updateProgress() {
        //如果播放列表为空

        int currentPosition = musicControl.getCurrentPosition();
        sb_progress.setProgress(currentPosition);
        if (musicControl.getCurrentPosition() == musicControl.getDuration()){
            playNext();
        }

        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS,1000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre_btn:
                playPre();
                break;
            case R.id.play_btn:
                play();
                break;
            case R.id.next_btn:
                playNext();
                break;
            case R.id.playingList_btn:
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;
        }

    }

    //显示歌曲列表弹窗
    private void showPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.playlist_popup_layout,null);
        mode = getModel();
        if (!mode){
            mode_image.setImageResource(R.drawable.list_mode);
            play_mode_title.setText("列表循环");
        } else {
            mode_image.setImageResource(R.drawable.single_mode);
            play_mode_title.setText("单曲循环");
        }
        playlistPopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
    }

    private boolean getModel() {
        return musicControl.getModel();
    }

    //控制播放器播放或暂停
    private void play() {
        musicControl.OnClick_Play(objectAnimator);
        updatePlayText();
        updateProgress();
    }
    //播放下一首
    private void playNext() {
        //从playingList中获取下一首歌的信息
        if (!getModel())
            playingSongList.moveToNext();
        musicControl.newPlay();
        //设置进度条的最大值
        sb_progress.setMax(musicControl.getDuration());
        //设置进度条的进度
        sb_progress.setProgress(musicControl.getCurrentPosition());
        musicControl.seekTo(0);
        UpdateView();
    }

    //播放上一首
    private void playPre() {
        //从playingList中获取上一首歌的信息
        playingSongList.moveToPre();
        musicControl.newPlay();
        //设置进度条的最大值
        sb_progress.setMax(musicControl.getDuration());
        //设置进度条的进度
        sb_progress.setProgress(musicControl.getCurrentPosition());
        musicControl.seekTo(0);
        UpdateView();
    }

    //异步检测
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS: {
                    updatePlayText();
                    updateProgress();
                    break;
                }
            }
        }
    };


}