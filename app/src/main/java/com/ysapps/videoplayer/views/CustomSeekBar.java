package com.ysapps.videoplayer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

import com.ysapps.videoplayer.Utils;

/**
 * Created by B.E.L on 11/09/2016.
 */

public class CustomSeekBar extends SeekBar {


    private Paint paint;
    private Rect bounds;


    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
       /* TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomSeekBar,
                0, 0);

        try {
            isInternal = a.getInteger(R.styleable.CustomSeekBar_memory, 0) == 0;
        } finally {
            a.recycle();
        }*/
        init();
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(sp2px(14));
        bounds = new Rect();
        float percentage =
                1 - (float) Utils.getAvailableInternalMemorySize() / (float) Utils.getTotalInternalMemorySize();
        setProgress((int) (percentage * 100));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String label = String.valueOf(getProgress()) + "%";
        paint.getTextBounds(label, 0, label.length(), bounds);
        float x = (float) getProgress() * (getWidth() - 2 * getThumbOffset()) / getMax() +
                (1 - (float) getProgress() / getMax()) * bounds.width() / 2
                + getThumbOffset() / (label.length() - 1);
        int middle = this.getHeight() / 2 + bounds.height() / 2;

        canvas.drawText(label, x, middle, paint);
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }


}
