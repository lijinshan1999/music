package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.SongListDetailActivity;
import com.example.music.entity.SongListVO;
import com.example.music.entity.SongVo;
import com.music.R;

import java.util.List;

public class SongListDetailAdapter extends BaseAdapter {

    private final List<SongVo> songList;
    private final Context mContext;

    public SongListDetailAdapter(Context context, int detail_song_item, List<SongVo> songVos) {
        this.mContext = context;
        this.songList = songVos;

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println(songList);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            //获取布局文件detail_song_item的根视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_song_item, null);
            viewHolder.song_operate = convertView.findViewById(R.id.song_operate);
            viewHolder.song_name = convertView.findViewById(R.id.song_name);
            viewHolder.singer_album = convertView.findViewById(R.id.singer_album);
            viewHolder.count = convertView.findViewById(R.id.count);
            convertView.setTag(viewHolder);   //将viewHolder存在convertView中
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); //重新获取ViewHolder
        }

        viewHolder.song_name.setText(songList.get(position).getName());
        viewHolder.singer_album.setText((CharSequence) songList.get(position).getAr().get(0));
        viewHolder.count.setText(String.valueOf(position + "1"));


        return convertView;
    }

    public final class ViewHolder {
        ImageView song_operate;
        TextView song_name;
        TextView singer_album;
        TextView count;

    }

}


