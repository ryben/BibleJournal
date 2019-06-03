package com.example.user.biblejournal.view.editnote

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

class ClickableMovementMethod : ArrowKeyMovementMethod() {

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt() - widget.totalPaddingLeft + widget.scrollX
            val y = event.y.toInt() - widget.totalPaddingTop + widget.scrollY

            val layout = widget.layout
            val row = layout.getLineForVertical(y)
            val col = layout.getOffsetForHorizontal(row, x.toFloat())

            val link = buffer.getSpans(col, col, ClickableSpan::class.java)

            if (link.size != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget)
                } else {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event)
    }

    companion object {

        private val sInstance = ClickableMovementMethod()

        val instance: MovementMethod
            get() = sInstance
    }
}