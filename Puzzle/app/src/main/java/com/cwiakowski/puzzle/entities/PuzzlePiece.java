package com.cwiakowski.puzzle.entities;

import android.content.Context;

public class PuzzlePiece extends android.support.v7.widget.AppCompatImageView {
    public int startingX;
    public int startingY;
    public int targetX;
    public int targetY;
    public int width;
    public int height;
    public boolean canMove = true;

    public PuzzlePiece(Context context) {
        super(context);
    }
}