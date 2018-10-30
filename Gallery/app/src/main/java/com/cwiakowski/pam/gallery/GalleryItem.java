package com.cwiakowski.pam.gallery;

public class GalleryItem {
    public String imageUri;
    public boolean isSelected = false;

    public GalleryItem(String imageUri) {
        this.imageUri = imageUri;
    }
}
