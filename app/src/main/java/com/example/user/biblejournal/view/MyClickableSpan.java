package com.example.user.biblejournal.view;

import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class MyClickableSpan extends ClickableSpan {
    private ClickableSpanListener clickListener;

    public MyClickableSpan(ClickableSpanListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickableSpanListener {
        void onClickableSpanClick();
    }

    @Override
    public void onClick(@NonNull View widget) {
        clickListener.onClickableSpanClick();
    }

}
