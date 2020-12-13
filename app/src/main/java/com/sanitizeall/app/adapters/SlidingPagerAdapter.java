package com.sanitizeall.app.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.sanitizeall.app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by rahul on 15/3/18.
 */

public class SlidingPagerAdapter extends PagerAdapter {

    private Context mContext;

    public SlidingPagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slide, container, false);

        ImageView imageView = view.findViewById(R.id.sliding_image);

        if (position == 0){
            Glide.with(mContext).load(R.drawable.img_one).into(imageView);
        }
        else if (position == 1){
            Glide.with(mContext).load(R.drawable.img_two).into(imageView);
        }
        else if (position == 2){
            Glide.with(mContext).load(R.drawable.img_three).into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
