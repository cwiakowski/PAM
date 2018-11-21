package com.cwiakowski.puzzle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class TouchListener implements View.OnTouchListener {

    private float xDelta;
    private float yDelta;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
        if(event.getAction() == event.ACTION_DOWN){
            xDelta = x - layoutParams.leftMargin;
            yDelta = x - layoutParams.topMargin;
        }
        else if (event.getAction() == event.ACTION_MOVE) {
            layoutParams.leftMargin = (int) (x - xDelta);
            layoutParams.topMargin = (int) (y - yDelta);
            v.setLayoutParams(layoutParams);
        }
        return true;
    }
}
