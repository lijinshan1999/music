package com.example.music.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.music.SongListDetailActivity;
import com.example.music.entity.SongListVO;
import com.example.music.util.URL2BitmapUtil;
import com.music.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindMusicFragment extends Fragment implements View.OnClickListener {

    private List<SongListVO> songListVOS;
    private TextView recommend_songlist_text1;
    private TextView recommend_songlist_text2;
    private TextView recommend_songlist_text3;
    private TextView recommend_songlist_text4;
    private TextView recommend_songlist_text5;
    private TextView recommend_songlist_text6;
    private ImageView recommend_songlist_image1;
    private ImageView recommend_songlist_image2;
    private ImageView recommend_songlist_image3;
    private ImageView recommend_songlist_image4;
    private ImageView recommend_songlist_image5;
    private ImageView recommend_songlist_image6;
    private Handler handler;
    private Runnable showRunnable;

    public FindMusicFragment() {
        // Required empty public constructor
    }

    public static FindMusicFragment newInstance(String param1, String param2) {
        FindMusicFragment fragment = new FindMusicFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View findmusicView = inflater.inflate(R.layout.fragment_find_music,container,false);

        handler = new Handler();
        showRunnable = new Runnable() {
            @Override
            public void run() {
                recommend_songlist_text1.setText(songListVOS.get(0).getName());
                recommend_songlist_text2.setText(songListVOS.get(1).getName());
                recommend_songlist_text3.setText(songListVOS.get(2).getName());
                recommend_songlist_text4.setText(songListVOS.get(3).getName());
                recommend_songlist_text5.setText(songListVOS.get(4).getName());
                recommend_songlist_text6.setText(songListVOS.get(5).getName());


                try {
                    recommend_songlist_image1.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(0).getCoverImgUrl()));
                    recommend_songlist_image2.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(1).getCoverImgUrl()));
                    recommend_songlist_image3.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(2).getCoverImgUrl()));
                    recommend_songlist_image4.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(3).getCoverImgUrl()));
                    recommend_songlist_image5.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(4).getCoverImgUrl()));
                    recommend_songlist_image6.setImageBitmap(URL2BitmapUtil.getBitmap(songListVOS.get(5).getCoverImgUrl()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        };

        getData();

        recommend_songlist_text1 = findmusicView.findViewById(R.id.recommend_songlist_text1);
        recommend_songlist_text2 = findmusicView.findViewById(R.id.recommend_songlist_text2);
        recommend_songlist_text3 = findmusicView.findViewById(R.id.recommend_songlist_text3);
        recommend_songlist_text4 = findmusicView.findViewById(R.id.recommend_songlist_text4);
        recommend_songlist_text5 = findmusicView.findViewById(R.id.recommend_songlist_text5);
        recommend_songlist_text6 = findmusicView.findViewById(R.id.recommend_songlist_text6);
        recommend_songlist_image1 = findmusicView.findViewById(R.id.recommend_songlist_image1);
        recommend_songlist_image2 = findmusicView.findViewById(R.id.recommend_songlist_image2);
        recommend_songlist_image3 = findmusicView.findViewById(R.id.recommend_songlist_image3);
        recommend_songlist_image4 = findmusicView.findViewById(R.id.recommend_songlist_image4);
        recommend_songlist_image5 = findmusicView.findViewById(R.id.recommend_songlist_image5);
        recommend_songlist_image6 = findmusicView.findViewById(R.id.recommend_songlist_image6);

        ImageView recommend_song_btn = findmusicView.findViewById(R.id.IV1); //每日推荐
        recommend_song_btn.setOnClickListener(this);
        recommend_songlist_image1.setOnClickListener(this);
        recommend_songlist_image2.setOnClickListener(this);
        recommend_songlist_image3.setOnClickListener(this);
        recommend_songlist_image4.setOnClickListener(this);
        recommend_songlist_image5.setOnClickListener(this);
        recommend_songlist_image6.setOnClickListener(this);


        return findmusicView;
    }

    private void getData() {
        String url = "https://service-3jnzriln-1258693374.cd.apigw.tencentcs.com/top/playlist?limit=10&order=hot";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                songListVOS = new ArrayList<>();

                String playlists = JSONObject.parseObject(response.body().string()).get("playlists").toString();
                songListVOS = JSON.parseArray(playlists,SongListVO.class);

                //System.out.println(playlists);
                //System.out.println(songListVOS.get(0).getCreator().getNickname());
                handler.post(showRunnable);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IV1:  //每日推荐

                break;
            case R.id.recommend_songlist_image1:
                showSonglistdetail(0);
                break;
            case R.id.recommend_songlist_image2:
                showSonglistdetail(1);
                break;
            case R.id.recommend_songlist_image3:
                showSonglistdetail(2);
                break;
            case R.id.recommend_songlist_image4:
                showSonglistdetail(3);
                break;
            case R.id.recommend_songlist_image5:
                showSonglistdetail(4);
                break;
            case R.id.recommend_songlist_image6:
                showSonglistdetail(5);
                break;
        }
    }

    private void showSonglistdetail(int position) {
        Intent intent = new Intent(getActivity(), SongListDetailActivity.class);
        intent.putExtra("songListID",songListVOS.get(position).getId());
        intent.putExtra("songListVOS", songListVOS.get(position));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }
}