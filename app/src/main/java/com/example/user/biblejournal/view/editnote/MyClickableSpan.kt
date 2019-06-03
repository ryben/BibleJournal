package com.example.user.biblejournal.view.editnote

import android.text.style.ClickableSpan
import android.view.View

class MyClickableSpan(private val clickListener: ClickableSpanListener) : ClickableSpan() {

    interface ClickableSpanListener {
        fun onClickableSpanClick()
    }

    override fun onClick(widget: View) {
        clickListener.onClickableSpanClick()
    }

}
