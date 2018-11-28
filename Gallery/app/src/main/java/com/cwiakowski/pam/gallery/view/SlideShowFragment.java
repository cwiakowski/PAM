package com.cwiakowski.pam.gallery.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cwiakowski.pam.gallery.R;
import com.cwiakowski.pam.gallery.adapters.SlideShowPagerAdapter;
import com.cwiakowski.pam.gallery.entity.GalleryItem;

import java.util.ArrayList;
import java.util.List;

//Fragment that containst bigger version of picture clicked in MainActivity
public class SlideShowFragment extends DialogFragment {
    private static final String ARG_CURRENT_POSITION = "position";
    private List<GalleryItem> galleryItems;
    //Adapter that is a controller for this fragment
    private SlideShowPagerAdapter mSlideShowPagerAdapter;
    //ViewPager that changes currently showed picture
    private ViewPager mViewPagerGallery;

    //currently showed picture
    private int mCurrentPosition;

    public SlideShowFragment() {
        // Required empty public constructor
    }

    //Returns instance of this Fragment
    public static SlideShowFragment newInstance(int position) {
        SlideShowFragment fragment = new SlideShowFragment();
        //Create bundle to contain data
        Bundle args = new Bundle();
        //Put data into bundle
        args.putInt(ARG_CURRENT_POSITION, position);
        //Loads up bundle into Fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialization of galleryItems list
        galleryItems = new ArrayList<>();
        if (getArguments() != null) {
            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            //ReadsData from MainActivity
            galleryItems = ((MainActivity) getActivity()).getGalleryItems();
        }
        setRetainInstance(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide_show, container, false);


        mViewPagerGallery = view.findViewById(R.id.viewPagerGallery);
        //Initialization of SlideShowPagerAdapter
        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);
        mViewPagerGallery.setAdapter(mSlideShowPagerAdapter);

        mViewPagerGallery.setCurrentItem(mCurrentPosition);
        mViewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int currentSelected = mViewPagerGallery.getCurrentItem();
                }

            }
        });
        //Setting up buttons and its listeners
        Button prev = (Button) view.findViewById(R.id.button2);
        Button next = (Button) view.findViewById(R.id.button3);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerGallery.setCurrentItem(mViewPagerGallery.getCurrentItem()-1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPagerGallery.setCurrentItem(mViewPagerGallery.getCurrentItem() +1);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        if(getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}