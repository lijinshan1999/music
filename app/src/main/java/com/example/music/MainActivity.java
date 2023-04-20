package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music.adapter.MyFragmentAdapter;
import com.example.music.fragment.FindMusicFragment;
import com.example.music.fragment.FriendsFragment;
import com.example.music.fragment.MyMusicFragment;
import com.music.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ImageView iv_title_find_music;
    private ImageView iv_title_my_music;
    private ImageView iv_title_friend;
    private ImageView iv_title_search;
    private TextView tv_bottm_player;
    private ViewPager vp_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //修改系统策略，放开所有的权限
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }
        //获取登录携带过来的数据


        initView();
        initContentFragment();

        //StaticSongList.Init();

    }

    private void initView() {
        iv_title_find_music = findViewById(R.id.iv_title_find_music);
        iv_title_my_music = findViewById(R.id.iv_title_my_music);
        iv_title_friend = findViewById(R.id.iv_title_friend);
        iv_title_search = findViewById(R.id.iv_title_search);
        tv_bottm_player = findViewById(R.id.tv_bottm_player);

        vp_content = findViewById(R.id.vp_content);
        Toolbar viewById = findViewById(R.id.toolbars);

        iv_title_find_music.setOnClickListener(this);
        iv_title_my_music.setOnClickListener(this);
        iv_title_friend.setOnClickListener(this);
        iv_title_search.setOnClickListener(this);
        tv_bottm_player.setOnClickListener(this);
    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new FindMusicFragment());
        mFragmentList.add(new MyMusicFragment());
        mFragmentList.add(new FriendsFragment());

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),mFragmentList);
        vp_content.setAdapter(adapter);
        vp_content.setOffscreenPageLimit(2);//设置预加载页面数量2
        vp_content.addOnPageChangeListener(this);

        setCurrentItem(0);
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
                Intent intentSearch = new Intent(this,SearchMainActivity.class);
                startActivity(intentSearch);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                break;
            case R.id.tv_bottm_player:
                Intent intent = new Intent(this,PlayerActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
                break;
        }
    }


    //在屏幕滚动过程中不断被调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //代表哪个页面被选中
    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }

    //这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP)
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurrentItem(int i){
        vp_content.setCurrentItem(i);
        iv_title_find_music.setSelected(false);
        iv_title_my_music.setSelected(false);
        iv_title_friend.setSelected(false);
        switch (i){
            case 0:
                iv_title_find_music.setSelected(true);
                break;
            case 1:
                iv_title_my_music.setSelected(true);
                break;
            case 2:
                iv_title_friend.setSelected(true);
                break;
        }
    }
}