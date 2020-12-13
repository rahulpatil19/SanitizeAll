package com.sanitizeall.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanitizeall.app.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroPagerAdapter extends PagerAdapter {

    private Context context;

    public IntroPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_intro_pager, container, false);

        ImageView imageView = layout.findViewById(R.id.intro_pager_image_view);
        TextView titleTxtView = layout.findViewById(R.id.intro_title_txt_view);
        TextView descriptionTxtView = layout.findViewById(R.id.intro_description_txt_view);

        if (position == 0){
            titleTxtView.setText(context.getString(R.string.intro_title_one));
            descriptionTxtView.setText(context.getString(R.string.intro_description_one));
            Glide.with(context).load(R.drawable.ic_launcher_background).into(imageView);
        }else  if (position == 1){
            titleTxtView.setText(context.getString(R.string.intro_title_two));
            descriptionTxtView.setText(context.getString(R.string.intro_description_two));
            Glide.with(context).load(R.drawable.ic_launcher_background).into(imageView);
        }else  if (position == 2){
            titleTxtView.setText(context.getString(R.string.intro_title_three));
            descriptionTxtView.setText(context.getString(R.string.intro_description_three));
            Glide.with(context).load(R.drawable.ic_launcher_background).into(imageView);
        }

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
