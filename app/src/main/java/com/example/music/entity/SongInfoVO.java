package com.example.music.entity;

public class SongInfoVO {

    private String url;

    private String level; //音质

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SongInfoVO{" +
                "url='" + url + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
