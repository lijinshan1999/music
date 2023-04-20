package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.music.entity.Song;
import com.music.R;

import java.util.List;

public class PlayListAdapter extends BaseAdapter implements View.OnClickListener {
    private final List<Song> songList;
    private final Context mContext;
    public InnerItemOnclickListener mListener;

    public PlayListAdapter(Context context, List<Song> playlist) {
        super();
        this.songList = playlist;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.playlist_item,null);
            viewHoLder.songlist_name = convertView.findViewById(R.id.songlist_name);
            viewHoLder.delete_btn = convertView.findViewById(R.id.delete_btn);
            convertView.setTag(viewHoLder);
        } else {
            viewHoLder = (ViewHoLder) convertView.getTag();
        }

        viewHoLder.songlist_name.setText(songList.get(position).getSongName() + "-" +songList.get(position).getSingerName());
        viewHoLder.delete_btn.setOnClickListener(this);
        viewHoLder.delete_btn.setTag(position);
        viewHoLder.songlist_name.setTag(position);

        return convertView;
    }
    class ViewHoLder{
        TextView songlist_name;
        ImageView delete_btn;
    }

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }


}
