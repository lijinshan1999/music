package com.example.music;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music.adapter.MyFragmentAdapter;
import com.example.music.fragment.SearchSongFragment;
import com.example.music.fragment.SearchSonglistFragment;
import com.example.music.fragment.SearchuserFragment;
import com.music.R;

import java.util.ArrayList;

public class SearchMainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View view_status;
    private TextView search_song_title;
    private TextView search_songlist_title;
    private TextView search_user_title;
    private ImageView search_song_line;
    private ImageView search_songlist_line;
    private ImageView search_user_line;
    private EditText search_editbox;
    private ViewPager vp_content;
    private Toolbar toolbars;
    private String searchKey;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }

        initView();

    }

    private void initView() {
        view_status=(View)findViewById(R.id.view_status);
        search_song_title=findViewById(R.id.search_song_title);
        search_songlist_title=findViewById(R.id.search_songlist_title);
        search_user_title=findViewById(R.id.search_user_title);
        vp_content=(ViewPager)findViewById(R.id.vp_content);
        toolbars=(Toolbar)findViewById(R.id.toolbars);
        search_song_line=findViewById(R.id.search_song_line);
        search_songlist_line=findViewById(R.id.search_songlist_line);
        search_user_line=findViewById(R.id.search_user_line);
        search_song_line.setVisibility(View.INVISIBLE);
        search_songlist_line.setVisibility(View.INVISIBLE);
        search_user_line.setVisibility(View.INVISIBLE);
        search_song_title.setOnClickListener(this);
        search_songlist_title.setOnClickListener(this);
        search_user_title.setOnClickListener(this);
        ImageView search_back= findViewById(R.id.back);

        search_editbox = findViewById(R.id.search_editbox);
        search_editbox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        search_editbox.setSingleLine(false); //设置搜索编辑框可以多行输入
        search_editbox.setHorizontallyScrolling(false); //禁止搜索编辑框在水平方向滚动
        search_editbox.setFocusable(true);
        search_editbox.setFocusableInTouchMode(true);
        search_editbox.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE); //禁用输入法中的“完成”按钮
        search_editbox.setSelection(search_editbox.getText().length()); //设置搜索编辑框中的文本光标位置为最后一位
        TextView search_btn=findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);

        TextView bottm_player_btn=findViewById(R.id.bottm_player_btn);
        bottm_player_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
                break;
            case R.id.search_btn:
                if(!search_editbox.getText().toString().equals("")){
                    hideKeyboard();
                    if(!search_editbox.getText().toString().equals(searchKey)){
                        searchKey =search_editbox.getText().toString();
                        bundle = new Bundle();
                        bundle.clear();
                        bundle.putString("searchkey", search_editbox.getText().toString());
                        initContentFragment();
                    }
                }
                break;
            case R.id.bottm_player_btn:
                Intent intent = new Intent(this,PlayerActivity.class);
                intent.putExtra("option","");
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
                break;

            case R.id.search_song_title:
                if(vp_content.getCurrentItem()!=0){
                    setCurrentItem(0);
                }
                break;
            case R.id.search_songlist_title:
                if(vp_content.getCurrentItem()!=1){
                    setCurrentItem(1);
                }
                break;
            case R.id.search_user_title:
                if(vp_content.getCurrentItem()!=2){
                    setCurrentItem(2);
                }
                break;
        }
    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new SearchSongFragment());
        mFragmentList.add(new SearchSonglistFragment());
        mFragmentList.add(new SearchuserFragment());
        mFragmentList.get(0).setArguments(bundle);
        mFragmentList.get(1).setArguments(bundle);
        mFragmentList.get(2).setArguments(bundle);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),mFragmentList);
        vp_content.setAdapter(adapter);
        vp_content.setOffscreenPageLimit(2);
        vp_content.addOnPageChangeListener(this);
        setSupportActionBar(toolbars); //允许许将Toolbar作为应用程序的ActionBar使用
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false); //不显示ActionBar的标题
        }
        setCurrentItem(0);
    }

    //关闭软键盘
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }

    private void setCurrentItem(int position) {
        vp_content.setCurrentItem(position);
        search_song_title.setSelected(false);
        search_songlist_title.setSelected(false);
        search_user_title.setSelected(false);
        switch (position){
            case 0:
                search_song_title.setSelected(true);
                search_song_line.setVisibility(View.VISIBLE);
                search_songlist_line.setVisibility(View.INVISIBLE);
                search_user_line.setVisibility(View.INVISIBLE);
                break;
            case 1:
                search_songlist_title.setSelected(true);
                search_song_line.setVisibility(View.INVISIBLE);
                search_songlist_line.setVisibility(View.VISIBLE);
                search_user_line.setVisibility(View.INVISIBLE);
                break;
            case 2:
                search_user_title.setSelected(true);
                search_song_line.setVisibility(View.INVISIBLE);
                search_songlist_line.setVisibility(View.INVISIBLE);
                search_user_line.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}