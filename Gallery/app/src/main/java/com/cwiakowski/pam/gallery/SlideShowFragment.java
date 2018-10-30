package com.cwiakowski.pam.gallery;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cwiakowski.pam.gallery.activities.MainActivity;
import com.cwiakowski.pam.gallery.entity.GalleryItem;

import java.util.ArrayList;
import java.util.List;

public class SlideShowFragment extends DialogFragment {
    //declare static variable which will serve as key of current position argument
    private static final String ARG_CURRENT_POSITION = "position";
    //Declare list of GalleryItems
    List<GalleryItem> galleryItems;
    // //Deceleration of  Slide show View Pager Adapter
    SlideShowPagerAdapter mSlideShowPagerAdapter;
    //Deceleration of viewPager
    ViewPager mViewPagerGallery;

    private int mCurrentPosition;
    //set bottom to visible of first load

    public SlideShowFragment() {
        // Required empty public constructor
    }

    //This method will create new instance of SlideShowFragment
    public static SlideShowFragment newInstance(int position) {
        SlideShowFragment fragment = new SlideShowFragment();
        //Create bundle
        Bundle args = new Bundle();
        //put Current Position in the bundle
        args.putInt(ARG_CURRENT_POSITION, position);
        //set arguments of SlideShowFragment
        fragment.setArguments(args);
        //return fragment instance
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialise GalleryItems List
        galleryItems = new ArrayList<>();
        if (getArguments() != null) {
            //get Current selected position from arguments
            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            //get GalleryItems from activity
            galleryItems = ((MainActivity) getActivity()).galleryItems;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        mViewPagerGallery = view.findViewById(R.id.viewPagerGallery);
        // set On Touch Listener on mViewPagerGallery to hide show bottom bar


        //Initialise View Pager Adapter
        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);
        //set adapter to Viewpager
        mViewPagerGallery.setAdapter(mSlideShowPagerAdapter);
        //Create GalleryStripRecyclerView's Layout manager

        //tell viewpager to open currently selected item and pass position of current item
        mViewPagerGallery.setCurrentItem(mCurrentPosition);
        //set image name textview's text according to position
        //Add OnPageChangeListener to viewpager to handle page changes
        mViewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //first check When Page is scrolled and gets stable
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //get current  item on view pager
                    int currentSelected = mViewPagerGallery.getCurrentItem();
                    //scroll strip smoothly to current  position of viewpager
                }

            }
        });
        return view;
    }

}