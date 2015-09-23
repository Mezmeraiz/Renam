package com.mezmeraiz.renam.animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by pc on 05.06.2015.
 */
public class HideableLayout extends FrameLayout {

    private float oldY;

    public HideableLayout(Context context) {
        super(context);
    }

    public HideableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HideableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HideableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    protected void hideLayout(){
        // скрываем кнопку
        this.animate().translationY(300).setDuration(500);

    }
    protected void showLayout(){
        // показываем кнопку
        this.animate().translationY(0).setDuration(500);
    }



}
