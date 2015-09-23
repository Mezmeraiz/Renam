package com.mezmeraiz.renam.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Layout с анимированной кнопкой
 */
public class AutoHideableLayout extends HideableLayout implements View.OnTouchListener{

    private float oldY;

    public AutoHideableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoHideableLayout(Context context) {
        super(context);
    }

    public AutoHideableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHideableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float newY =  event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if (newY<oldY){
                    hideLayout();
                }else{
                    showLayout();
                }
        }
        oldY=newY;
         return false;
    }
}

