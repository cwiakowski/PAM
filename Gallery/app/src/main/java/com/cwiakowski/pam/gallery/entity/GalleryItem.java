package com.cwiakowski.pam.gallery.entity;

//An entity class of Items displayed in Gallery
public class GalleryItem {
    private String imageUri;
    private boolean isSelected = false;

    public GalleryItem(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
