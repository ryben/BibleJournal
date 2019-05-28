package com.example.user.biblejournal.view.editnote;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class ClickableMovementMethod extends ArrowKeyMovementMethod {

    private static ClickableMovementMethod sInstance = new ClickableMovementMethod();

    public static MovementMethod getInstance() {
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX() - widget.getTotalPaddingLeft() + widget.getScrollX();
            int y = (int) event.getY() - widget.getTotalPaddingTop() + widget.getScrollY();

            Layout layout = widget.getLayout();
            int row = layout.getLineForVertical(y);
            int col = layout.getOffsetForHorizontal(row, x);

            ClickableSpan[] link = buffer.getSpans(col, col, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else {
                    Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}