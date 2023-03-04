package com.example.music.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SongVo {

    private String name;
    private String id;

    private ArrayList<SongInfoArray> ar;

    public SongVo(String name, String id, ArrayList<SongInfoArray> ar) {
        this.name = name;
        this.id = id;
        this.ar = ar;
    }

    public SongVo(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public SongVo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<SongInfoArray> getAr() {
        return ar;
    }

    public void setAr(ArrayList<SongInfoArray> ar) {
        this.ar = ar;
    }

    @Override
    public String toString() {
        return "SongVo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", ar=" + ar +
                '}';
    }

    public class SongInfoArray {
        private String name;
        private String id;

        public SongInfoArray() {
        }

        public SongInfoArray(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SongInfoArray(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SongInfoArray{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}


