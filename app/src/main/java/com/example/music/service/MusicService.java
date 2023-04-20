package com.example.music.service;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.media.MediaDescrambler;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.entity.Song;
import com.example.music.entity.SongInfoVO;
import com.example.music.entity.SongVo;
import com.example.music.singleton.PlayingSongList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicService extends Service {

    private final MediaPlayer player = new MediaPlayer();

    private List<SongInfoVO> songInfoVO = new ArrayList<>();
    private Runnable runnable;
    Handler handler = new Handler();

    private PlayingSongList playingSongList = PlayingSongList.getInstance();

    public MusicService() {

    }

    @Override
    public void onCreate() {
//        if (player == null) {
//            player = new MediaPlayer();
//        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (player == null) {
//            player = new MediaPlayer();
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.pause();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    //用于实现进程间通信（IPC）。它是一个低级别的通信机制，可用于将不同进程中的对象相互绑定在一起。
    public class MyBinder extends Binder {

        public MediaPlayer getPlayer() {
            return player;
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void changeModel() {
            player.setLooping(!player.isLooping());
        }

        public Boolean getModel() {
            return player.isLooping();
        }

        public void OnClick_Play(ObjectAnimator objectAnimator) {
            if (player == null) {
                newPlay();
            } else {
                if (player.isPlaying()) {
                    player.pause();
                    objectAnimator.pause();
                } else {
                    player.start();
                    objectAnimator.resume();
                }
            }
        }

        //从播放列表中移除当前歌曲
        public void remove() {

        }

        //从播放列表中移除指定歌曲
        public void remove(int index) {

        }

        //停止播放
        public void stop() {
            if (player.isPlaying()) {
                player.stop();
                player.seekTo(0);
            }
        }

        //获取当前歌曲的最大进度限制
        public int getDuration() {
            return player.getDuration();
        }

        //获取当前的播放进度
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }

        //调整当前的播放进度
        public void seekTo(int mesc) {
            player.seekTo(mesc);
        }

        //播放当前播放列表指定的当前歌曲
        public void newPlay() {

            runnable = new Runnable() {
                @Override
                public void run() {
                    String songUrl = songInfoVO.get(0).getUrl();
                    try {
                        player.reset(); //重置MediaPlayer
                        player.setDataSource(songUrl);
                        player.prepare();
                        player.start();
                        player.seekTo(0);

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"播放失败",Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
            };

            if (player == null) {
                Toast.makeText(getApplicationContext(), String.valueOf("播放列表为空"), Toast.LENGTH_SHORT).show();
                return;
            }

            if (playingSongList.getSize() == 0) {
                Toast.makeText(getApplicationContext(), String.valueOf("播放列表为空"), Toast.LENGTH_SHORT).show();
                return;
            }

            String songID = playingSongList.getNow().getSongID();

            getSongUrl(songID);
            System.out.println(songInfoVO);


        }

    }

    private void getSongUrl(String songID) {
        String url = "https://service-3jnzriln-1258693374.cd.apigw.tencentcs.com/song/url/v1?id="+songID+"&level=standard";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = JSONObject.parseObject(response.body().string()).get("data").toString();
                SongInfoVO song = JSON.parseArray(data,SongInfoVO.class).get(0);
                List<SongInfoVO> songs = JSON.parseArray(data,SongInfoVO.class);

                songInfoVO.clear();
                songInfoVO.addAll(songs);

               Looper.prepare();
                handler.post(runnable);
                Looper.loop();
            }
        });
        System.out.println("获取到：" + songInfoVO);
    }

}