package com.zt.rainbowweather.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Chu on 2017/8/22.
 */

@SuppressLint("AppCompatCustomView")
public class ShapeTextView extends TextView {

    public ShapeTextView(Context context) {
        super(context);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSuperShapeView(attrs);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSuperShapeView(attrs);
    }

    private void initSuperShapeView(AttributeSet attrs) {
        new SuperConfig().beSuperView(attrs, this);
    }
}
