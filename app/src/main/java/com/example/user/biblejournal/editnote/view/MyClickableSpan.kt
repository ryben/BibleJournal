package com.example.user.biblejournal.editnote.view

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.example.user.biblejournal.model.data.VerseAddress

class MyClickableSpan(private val clickListener: ClickableSpanListener, private val verseAddress: VerseAddress) : ClickableSpan() {

    interface ClickableSpanListener {
        fun onClickableSpanClick(verseAddress: VerseAddress)
    }

    override fun onClick(widget: View) {
        clickListener.onClickableSpanClick(verseAddress)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

}
