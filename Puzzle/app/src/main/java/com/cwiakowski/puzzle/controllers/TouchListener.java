package com.cwiakowski.puzzle.controllers;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cwiakowski.puzzle.R;
import com.cwiakowski.puzzle.entities.PuzzlePiece;
import com.cwiakowski.puzzle.utils.ScreenUtils;
import com.cwiakowski.puzzle.views.PuzzleActivity;

import static com.cwiakowski.puzzle.views.PuzzleActivity.imageViewH;
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

        ImageView imageView = view.findViewById(R.id.imageView);
        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
                    if (lParams.topMargin <= 0)
                        lParams.topMargin = 0;
                    if (lParams.leftMargin + piece.width > 1794)
                        lParams.leftMargin = 1794 - piece.width;
                    if (lParams.topMargin + piece.height > imageViewH+30)
                        lParams.topMargin = imageViewH - piece.height + 30;
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
                    } else if (lParams.leftMargin > imageViewH + 15) {
                        piece.startingX = lParams.leftMargin;
                        piece.startingY = lParams.topMargin;
                    } else {
                        lParams.leftMargin = piece.startingX;
                        lParams.topMargin = piece.startingY;
                        view.setLayoutParams(lParams);
                    }
                    break;
            }
        } else {
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
                    if (lParams.topMargin <= 0)
                        lParams.topMargin = 0;
                    if (lParams.leftMargin + piece.width > PuzzleActivity.imageViewW)
                        lParams.leftMargin = PuzzleActivity.imageViewW - piece.width;
                    if (lParams.topMargin + piece.height > 1572)
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
                    } else if (lParams.topMargin > imageViewH) {
                        piece.startingX = lParams.leftMargin;
                        piece.startingY = lParams.topMargin;
                    } else {
                        lParams.leftMargin = piece.startingX;
                        lParams.topMargin = piece.startingY;
                        view.setLayoutParams(lParams);
                    }
                    break;
            }
        }
        return true;
    }

}