package com.something.fitnessapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class Graph extends View {

    public Graph(Context c) { super(c); }
    public Graph(Context c, AttributeSet as) { super(c, as); }
    public Graph(Context c, AttributeSet as, int default_style) { super(c, as, default_style); }

    public void onMeasure(int width, int height) {

        /* Force graph to be square */
        if (width < height)
            super.onMeasure(width, width);
        else super.onMeasure(height, height);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* Get graph size */
        int width = getMeasuredWidth();
        float height = getMeasuredHeight();

        /* Draw graph if at least 1 km has been done */
        if (!timePerKm.isEmpty()) {

            /* Compute bars width & set stroke */
            int kmWidth = width / timePerKm.size();
            paint.setStrokeWidth(kmWidth);

            /* Compute number of pixels for 1ms */
            float unit = height / maxTime;

            /* Graph margin workaround */
            float offset = kmWidth / 2;

            /* Draw line, switch colour & advance offset to draw next bar */
            for (Object time : timePerKm) { /* Convert long to pixels */
                canvas.drawLine(offset, height - (unit * (long) time), offset, height, paint);
                paint.setColor(paint.getColor() == colours[0] ? colours[1] : colours[0]);
                offset += kmWidth;
            }
        }
    }

    /* Graph appearance variables */
    private Paint paint = new Paint();
    private int[] colours = { Color.DKGRAY, Color.LTGRAY };

    /* Time per kilometer */
    static ArrayList timePerKm;
    static long maxTime;
}