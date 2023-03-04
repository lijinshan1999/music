package com.example.music.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TextViewRoll extends AppCompatTextView {
    public TextViewRoll(@NonNull Context context) {
        super(context);
    }

    public TextViewRoll(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRoll(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //必须重写，且返回值是true，表示始终获取焦点
    @Override
    public boolean isFocused() {
        return super.isFocused();
    }
}
