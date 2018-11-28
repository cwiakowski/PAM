package com.cwiakowski.puzzle.views;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cwiakowski.puzzle.R;
import com.cwiakowski.puzzle.controllers.TouchListener;
import com.cwiakowski.puzzle.entities.PuzzlePiece;
import com.cwiakowski.puzzle.utils.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;

public class PuzzleActivity extends AppCompatActivity {
    ArrayList<PuzzlePiece> pieces;
    String mCurrentPhotoPath;
    String mCurrentPhotoUri;
    boolean visible = false;
    public static int imageViewH;
    public static int imageViewW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setMaxHeight(ScreenUtils.getScreenHeight(this));
        final RelativeLayout layout = findViewById(R.id.layout);
        readPictureFromFile(imageView, layout);
        initImageView(imageView);
    }

    private void initImageView(final ImageView imageView) {
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(visible)
                {
                    visible = false;
                    imageView.setAlpha(0f);
                }
                else {
                    visible = true;
                    imageView.setAlpha(0.5f);
                }
                return false;
            }
        });
    }

    private void readPictureFromFile(final ImageView imageView, final RelativeLayout layout) {
        Intent intent = getIntent();
        final String assetName = intent.getStringExtra("assetName");

        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (assetName != null) {
                    setPicFromAsset(assetName, imageView);
                }
                pieces = splitImage();
                TouchListener touchListener = new TouchListener(PuzzleActivity.this);
                Collections.shuffle(pieces);
                int orientation = getResources().getConfiguration().orientation;
                for (PuzzlePiece piece : pieces) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        lParams.leftMargin = imageViewW + new Random().nextInt((layout.getWidth() - piece.width - imageViewH)/10)*10;
                        piece.startingX = lParams.leftMargin;
                        lParams.topMargin = new Random().nextInt((layout.getHeight()-piece.height)/40)*40;
                        piece.startingY = lParams.topMargin;
                        piece.setLayoutParams(lParams);
                    } else {
                        lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.width);
                        piece.startingX = lParams.leftMargin;
                        lParams.topMargin = layout.getHeight() - piece.height - new Random().nextInt(260);
                        piece.startingY = lParams.topMargin;
                        piece.setLayoutParams(lParams);
                    }


                }
            }
        });
    }

    private void setPicFromAsset(String assetName, ImageView imageView) {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        imageViewH = targetH + imageView.getPaddingTop();
        imageViewW = targetW + 30;

        AssetManager am = getAssets();
        try {
            InputStream is = am.open("img/" + assetName);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            is.reset();

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<PuzzlePiece> splitImage() {
        int piecesNumber = 16;
        int rows = 4;
        int cols = 4;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.targetX = xCoord + imageView.getLeft();
                piece.targetY = yCoord + imageView.getTop();
                piece.width = pieceWidth;
                piece.height = pieceHeight;
                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void checkGameOver() {
        if (isGameOver()) {
            finish();
        }
    }

    private boolean isGameOver() {
        for (PuzzlePiece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }
        return true;
    }
}
