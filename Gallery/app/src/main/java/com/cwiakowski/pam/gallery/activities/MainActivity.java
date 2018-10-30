package com.cwiakowski.pam.gallery.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cwiakowski.pam.gallery.SlideShowFragment;
import com.cwiakowski.pam.gallery.adapters.GalleryAdapter;
import com.cwiakowski.pam.gallery.R;
import com.cwiakowski.pam.gallery.entity.GalleryItem;
import com.cwiakowski.pam.gallery.utilities.GalleryUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {

    //List of pictures
    private List<GalleryItem> galleryItems;
    private static final int RC_READ_STORAGE = 5;
    //Adapter that maneges pictures
    private GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization of RecyclerView
        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));

        //Initialization of GalleryAdapter
        mGalleryAdapter = new GalleryAdapter(this);
        recyclerViewGallery.setAdapter(mGalleryAdapter);

        //Checking if app have permission to read storage data, optionally asking for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            galleryItems = GalleryUtils.getImages(this);
            mGalleryAdapter.addGalleryItems(galleryItems);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }


    }

    //Opens up a Fragment when thumbnail is clicked
    @Override
    public void onItemSelected(int position) {
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        slideShowFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Reads data when permission is granted
                galleryItems = GalleryUtils.getImages(this);
                mGalleryAdapter.addGalleryItems(galleryItems);
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public List<GalleryItem> getGalleryItems() {
        return galleryItems;
    }
}