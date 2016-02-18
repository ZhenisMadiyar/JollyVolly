package com.kz.jv.jollyvolly;

/**
 * Created by madiyarzhenis on 01.09.15.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoneSwipableViewPager extends ViewPager {

    public NoneSwipableViewPager(Context context) {
        super(context);
    }

    public NoneSwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
}