package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_title_find_music;
    private ImageView iv_title_my_music;
    private ImageView iv_title_friend;
    private ImageView iv_title_search;
    private TextView tv_bottm_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }
        //获取登录携带过来的数据


        initView();


    }

    private void initView() {
        iv_title_find_music = findViewById(R.id.iv_title_find_music);
        iv_title_my_music = findViewById(R.id.iv_title_my_music);
        iv_title_friend = findViewById(R.id.iv_title_friend);
        iv_title_search = findViewById(R.id.iv_title_search);
        tv_bottm_player = findViewById(R.id.tv_bottm_player);

        iv_title_find_music.setOnClickListener(this);
        iv_title_my_music.setOnClickListener(this);
        iv_title_friend.setOnClickListener(this);
        iv_title_search.setOnClickListener(this);
        tv_bottm_player.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_title_find_music:
                break;
            case R.id.iv_title_my_music:
                break;
            case R.id.iv_title_friend:
                break;
            case R.id.iv_title_search:
                break;
            case R.id.tv_bottm_player:
                break;
        }
    }
}