package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.music.R;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        String songID = getIntent().getStringExtra("songID");
        String songName = getIntent().getStringExtra("songName");
        System.out.println("songID:" + songID + "\nsongName:"+ songName);
    }
}