package com.example.music.entity;

import android.os.Parcelable;

import java.io.Serializable;

public class SongListVO implements Serializable {

    private String name;

    private String id;
    private String coverImgUrl;

    private CreatorVO creator;

    public CreatorVO getCreator() {
        return creator;
    }

    public void setCreator(CreatorVO creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SongListVO() {
    }

    public SongListVO(String name, String id, String coverImgUrl) {
        this.name = name;
        this.id = id;
        this.coverImgUrl = coverImgUrl;
    }

//    @Override
//    public String toString() {
//        return "SongListVO{" +
//                "name='" + name + '\'' +
//                ", id='" + id + '\'' +
//                ", coverImgUrl='" + coverImgUrl + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "SongListVO{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", creato=" + creator +
                '}';
    }
}
