package com.example.music.entity;

import java.util.ArrayList;

public class SearchSongVO {

    private String id;
    private String name;
   private ArrayList<Artists> artists;

    public SearchSongVO() {
    }

    public ArrayList<Artists> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artists> artists) {
        this.artists = artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SearchSongVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                '}';
    }

    public class Artists{

        String id;
        String name;
        String img1v1Url;

        public String getImg1v1Url() {
            return img1v1Url;
        }

        public void setImg1v1Url(String img1v1Url) {
            this.img1v1Url = img1v1Url;
        }

        public Artists() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Artists(String id, String name, String img1v1Url) {
            this.id = id;
            this.name = name;
            this.img1v1Url = img1v1Url;
        }
    }
}
