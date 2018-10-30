package com.cwiakowski.pam.gallery.utilities;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.cwiakowski.pam.gallery.entity.GalleryItem;

import java.util.ArrayList;
import java.util.List;

//Class containing utilities to get pictures from File
public class GalleryUtils {

    //File path to folder with pictures
    private static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/Download";

    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    //Returning list of pictures
    public static List<GalleryItem> getImages(Context context) {
        //Setting up data to save, DISPLAY_NAME is not used at this point
        final String[] projection = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {GalleryUtils.getBucketId(CAMERA_IMAGE_BUCKET_NAME)};
        //Setting up tool to extract data from file
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        //Initialization of ArrayList containing pictures
        ArrayList<GalleryItem> result = new ArrayList<GalleryItem>(cursor.getCount());
        //Saving data in ArrayList
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //final int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            do {
                GalleryItem galleryItem = new GalleryItem(cursor.getString(dataColumn));
                result.add(galleryItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;

    }
}