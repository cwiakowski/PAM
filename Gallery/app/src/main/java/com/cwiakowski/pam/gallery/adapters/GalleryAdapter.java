package com.cwiakowski.pam.gallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cwiakowski.pam.gallery.R;
import com.cwiakowski.pam.gallery.entity.GalleryItem;
import com.cwiakowski.pam.gallery.utilities.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter {
    //List of pictures
    private List<GalleryItem> galleryItems;

    private Context context;
    //Allows for communication with MainActiivity
    private GalleryAdapterCallBacks mAdapterCallBacks;

    public GalleryAdapter(Context context) {
        this.context = context;
        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
        this.galleryItems = new ArrayList<>();
    }

    //Adds new picture, when new one is added to folder
    public void addGalleryItems(List<GalleryItem> galleryItems) {
        int previousSize = this.galleryItems.size();
        this.galleryItems.addAll(galleryItems);
        notifyItemRangeInserted(previousSize, galleryItems.size());

    }

    //Inflate the layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_item, parent, false);
        return new GalleryItemHolder(row);
    }

    //Fills the layout with pictures
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GalleryItem currentItem = galleryItems.get(position);
        File imageViewThoumb = new File(currentItem.getImageUri());
        GalleryItemHolder galleryItemHolder = (GalleryItemHolder) holder;
        Picasso.get()
                .load(imageViewThoumb)
                .centerCrop()
                .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)
                .into(galleryItemHolder.imageViewThumbnail);
        galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterCallBacks.onItemSelected(position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class GalleryItemHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;

        public GalleryItemHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);

        }
    }

    public interface GalleryAdapterCallBacks {
        void onItemSelected(int position);
    }

}

