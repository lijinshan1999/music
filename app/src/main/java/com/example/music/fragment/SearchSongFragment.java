package com.example.music.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.PlayerActivity;
import com.example.music.adapter.SearchSongAdapter;
import com.example.music.entity.SearchSongVO;
import com.example.music.entity.Song;
import com.example.music.entity.SongInfoVO;
import com.example.music.singleton.PlayingSongList;
import com.music.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchSongFragment extends Fragment implements SearchSongAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {

    private List<Song> searchsongList = new ArrayList<Song>();
    private View searchsongView;
    private View searchPopupView;
    private PopupWindow searchPopupWindow;
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private int currentPosition;
    private Handler handler;
    private Bundle bundle;
    private String searchKey;
    private ListView searchsongListview;

    List<SearchSongVO> searchSongVOS;
    private Runnable show;
    private SearchSongAdapter searchSongAdapter;

    public SearchSongFragment() {
        // Required empty public constructor
    }


    public static SearchSongFragment newInstance(String param1, String param2) {
        SearchSongFragment fragment = new SearchSongFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searchsongView = inflater.inflate(R.layout.fragment_search_song, container, false);
        initPopwindow();
        handler = new Handler();
        show = new Runnable() {
            @Override
            public void run() {
                //更新界面

                searchSongAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(searchsongListview);
            }
        };

        bundle = this.getArguments();
        searchKey = bundle.getString("searchkey");
        searchsongList.clear();
        getJsonData();
        searchsongListview = searchsongView.findViewById(R.id.search_song_list_view);

        searchSongAdapter = new SearchSongAdapter(this.getActivity(),searchsongList);
        searchSongAdapter.setOnInnerItemOnClickListener(this);
        searchsongListview.setAdapter(searchSongAdapter);

        searchsongListview.setOnItemClickListener(this);

        return searchsongView;
    }

    private void getJsonData() {
        String url = "https://service-3jnzriln-1258693374.cd.apigw.tencentcs.com/search?keywords="+searchKey+"&type=1&limit=20";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                searchsongList.clear();
                String result = JSONObject.parseObject(response.body().string()).get("result").toString();
                String songs = JSONObject.parseObject(result).get("songs").toString();
                searchSongVOS = JSON.parseArray(songs,SearchSongVO.class);

                for (int i = 0;i < searchSongVOS.size();i++){
                    Song song = new Song();
                    song.setSongID(searchSongVOS.get(i).getId());
                    song.setSongName(searchSongVOS.get(i).getName());
                    song.setSingerID(searchSongVOS.get(i).getArtists().get(0).getId());
                    song.setSingerName(searchSongVOS.get(i).getArtists().get(0).getName());
                    song.setSongImage(searchSongVOS.get(i).getArtists().get(0).getImg1v1Url());
                    searchsongList.add(song);

                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }

    private void initPopwindow() {
        //搜索歌曲操作弹窗
        searchPopupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.search_song_popup_layout, null, false);
        searchPopupWindow = new PopupWindow(searchPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        searchPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        searchPopupWindow.setFocusable(true);
        searchPopupWindow.setTouchable(true);
        searchPopupWindow.setOutsideTouchable(true);
        searchPopupWindow.setAnimationStyle(R.style.animTranslate);
        searchPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out= searchPopupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopupWindow.dismiss();
            }
        });
        //下一首播放
        TextView pop_next_play= searchPopupView.findViewById(R.id.next_play_btn_area);
        pop_next_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playingSongList.addNext(searchsongList.get(currentPosition));
                searchPopupWindow.dismiss();
            }
        });
        //跳转到分享歌单活动
        TextView pop_share= searchPopupView.findViewById(R.id.pop_share_btn_area);
        pop_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopupWindow.dismiss();

            }
        });

    }


    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
    }

    //listView条目的单击事件
    @Override
    public void itemClick(View view) {
        switch (view.getId()){
            case R.id.search_song_operate:
                currentPosition = (int) view.getTag();
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;

        }
    }

    //显示底部弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_search_main, null);
        TextView pop_song_title =searchPopupView.findViewById(R.id.pop_song_title);
        String pop_song_str="单曲："+searchsongList.get(currentPosition).getSongName();
        pop_song_title.setText(pop_song_str);
        searchPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    //根据其内容自适应高度
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPosition = position;
        if (playingSongList.getSize() == 0){
            List<Song> tempsongList = new ArrayList<Song>();
            tempsongList.add(searchsongList.get(currentPosition));
            playingSongList.InitPlayingSongList(tempsongList,"search",0);
        }
        if(!playingSongList.getNow().getSongID().equals(searchsongList.get(currentPosition).getSongID())){
            playingSongList.addNext(searchsongList.get(currentPosition));
            playingSongList.moveToNext();
        }
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("option","next");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }
}