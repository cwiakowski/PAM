package com.cwiakowski.puzzle.controllers;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cwiakowski.puzzle.R;
import com.cwiakowski.puzzle.entities.PuzzlePiece;
import com.cwiakowski.puzzle.views.PuzzleActivity;

import static java.lang.Math.abs;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private PuzzleActivity activity;

    public TouchListener(PuzzleActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double maxOffset = 45;

        PuzzlePiece piece = (PuzzlePiece) view;
        if (!piece.canMove) {
            return true;
        }

        RelativeLayout layout = view.findViewById(R.id.layout);
        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:

                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                if (lParams.leftMargin <= 0)
                    lParams.leftMargin = 0;
                if(lParams.topMargin <= 0)
                    lParams.topMargin = 0;
                if (lParams.leftMargin + piece.width > 1080)
                    lParams.leftMargin = 1080 - piece.width;
                if (lParams.topMargin + piece.height >1572)
                    lParams.topMargin = 1572 - piece.height;
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                int offsetX = StrictMath.abs(piece.targetX - lParams.leftMargin);
                int offsetY = StrictMath.abs(piece.targetY - lParams.topMargin);
                if (offsetX <= maxOffset && offsetY <= maxOffset) {
                    lParams.leftMargin = piece.targetX;
                    lParams.topMargin = piece.targetY;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    activity.checkGameOver();
                }
                else if (lParams.topMargin > 1050){
                    piece.startingX = lParams.leftMargin;
                    piece.startingY = lParams.topMargin;
                } else {
                    lParams.leftMargin = piece.startingX;
                    lParams.topMargin = piece.startingY;
                    view.setLayoutParams(lParams);
                }
                break;
        }

        return true;
    }

}