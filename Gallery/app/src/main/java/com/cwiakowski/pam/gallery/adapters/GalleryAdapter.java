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
        //Declare GalleryItems List
        List<GalleryItem> galleryItems;
        Context context;
        //Declare GalleryAdapterCallBacks
        GalleryAdapterCallBacks mAdapterCallBacks;

        public GalleryAdapter(Context context) {
            this.context = context;
            //get GalleryAdapterCallBacks from contex
            this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
            //Initialize GalleryItem List
            this.galleryItems = new ArrayList<>();
        }

        //This method will take care of adding new Gallery items to RecyclerView
        public void addGalleryItems(List<GalleryItem> galleryItems) {
            int previousSize = this.galleryItems.size();
            this.galleryItems.addAll(galleryItems);
            notifyItemRangeInserted(previousSize, galleryItems.size());

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View row = inflater.inflate(R.layout.custiom_row_gallery_item, parent, false);
            return new GalleryItemHolder(row);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //get current Gallery Item
            GalleryItem currentItem = galleryItems.get(position);
            //Create file to load with Picasso lib
            File imageViewThoumb = new File(currentItem.imageUri);
            //cast holder with gallery holder
            GalleryItemHolder galleryItemHolder = (GalleryItemHolder) holder;
            //Load with Picasso
            Picasso.get()
                    .load(imageViewThoumb)
                    .centerCrop()
                    .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)//Resize image to width half of screen and height 1/3 of screen height
                    .into(galleryItemHolder.imageViewThumbnail);
            //set name of Image
            //set on click listener on imageViewThumbnail
            galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //call onItemSelected method and pass the position and let activity decide what to do when item selected
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

        //Interface for communication of Adapter and MainActivity
        public interface GalleryAdapterCallBacks {
            void onItemSelected(int position);
        }


    }
