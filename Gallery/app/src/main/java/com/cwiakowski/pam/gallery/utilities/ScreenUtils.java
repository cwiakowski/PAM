package com.cwiakowski.pam.gallery.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

//Class contains utility methods to get information about screen size of phone
public class ScreenUtils {

    //returns Witdth of screen
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    //returns height of screen
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}