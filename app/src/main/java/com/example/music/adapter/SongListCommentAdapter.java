package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.music.SongListCommentActivity;
import com.example.music.entity.SongListCommentVO;
import com.example.music.util.URL2BitmapUtil;
import com.example.music.view.ImageViewCircle;
import com.music.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongListCommentAdapter extends BaseAdapter {

    private List<SongListCommentVO> comment;
    private Context context;

    public SongListCommentAdapter(Context context, List<SongListCommentVO> songListCommentVOS) {
        this.context = context;
        this.comment = songListCommentVOS;
    }

    @Override
    public int getCount() {
        return comment.size();
    }

    @Override
    public Object getItem(int position) {
        return comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.songlist_comment_item, null);
            viewHolder.comment_imageHead = convertView.findViewById(R.id.comment_imageHead);
            viewHolder.comment_nickname = convertView.findViewById(R.id.comment_nickname);
            viewHolder.comment_datetime = convertView.findViewById(R.id.comment_datetime);
            viewHolder.comment_content = convertView.findViewById(R.id.comment_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.comment_imageHead.setImageBitmap(URL2BitmapUtil.getBitmap(comment.get(position).getUser().getAvatarUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        viewHolder.comment_nickname.setText(comment.get(position).getUser().getNickname());
        viewHolder.comment_datetime.setText(comment.get(position).getTimeStr());
        viewHolder.comment_content.setText(comment.get(position).getContent());

        return convertView;
    }

    class ViewHolder {
        ImageViewCircle comment_imageHead;
        TextView comment_nickname;
        TextView comment_datetime;
        TextView comment_content;
    }
}
