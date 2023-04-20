package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music.entity.Song;
import com.example.music.fragment.SearchSongFragment;
import com.music.R;

import java.util.List;

public class SearchSongAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<Song> searchSongList;

    public InnerItemOnclickListener mListener;

    public SearchSongAdapter(Context context, List<Song> searchsongList) {
        this.mContext = context;
        this.searchSongList = searchsongList;
    }

    @Override
    public int getCount() {
        return searchSongList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchSongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoLder viewHoLder;
        if (convertView == null){
            viewHoLder = new ViewHoLder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_song_item,null);
            viewHoLder.search_song_operate = convertView.findViewById(R.id.search_song_operate);
            viewHoLder.song_name = convertView.findViewById(R.id.song_name);
            viewHoLder.singer_name = convertView.findViewById(R.id.singer_album);
            convertView.setTag(viewHoLder);
        } else {
            viewHoLder = (ViewHoLder) convertView.getTag();
        }

        viewHoLder.song_name.setText(searchSongList.get(position).getSongName());
        viewHoLder.singer_name.setText(String.valueOf(searchSongList.get(position).getSingerName()));
        viewHoLder.search_song_operate.setOnClickListener(this);
        viewHoLder.search_song_operate.setTag(position);

        return convertView;
    }

    public interface InnerItemOnclickListener{
        void itemClick(View view);
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    class ViewHoLder{
        ImageView search_song_operate;
        TextView song_name;
        TextView singer_name;
    }

}
