package com.example.music.entity;

import java.io.Serializable;

public class CreatorVO implements Serializable {

    private String nickname;

    public CreatorVO(String nickname) {
        this.nickname = nickname;
    }

    public CreatorVO() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "CreatorVO{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
