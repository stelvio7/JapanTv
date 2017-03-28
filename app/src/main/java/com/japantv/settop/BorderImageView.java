package com.japantv.settop;

import com.japantv.settop1.R; 

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BorderImageView extends ImageView {

    public BorderImageView(Context context) {
        super(context);
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable border = getResources().getDrawable(R.drawable.border);
        border.setBounds(canvas.getClipBounds());

        border.draw(canvas);
    }
}
