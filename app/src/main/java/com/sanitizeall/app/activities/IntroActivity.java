package com.sanitizeall.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sanitizeall.app.adapters.IntroPagerAdapter;
import com.sanitizeall.app.R;

public class IntroActivity extends AppCompatActivity {

    private IntroPagerAdapter introPagerAdapter;
    private CircleIndicator circleIndicator;
    private TextView continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ViewPager viewPager = findViewById(R.id.intro_view_pager);
        circleIndicator = findViewById(R.id.circle_indicator);
        continueBtn = findViewById(R.id.intro_continue_btn);

        introPagerAdapter = new IntroPagerAdapter(this);
        viewPager.setAdapter(introPagerAdapter);

        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    circleIndicator.setVisibility(View.VISIBLE);
                    continueBtn.setVisibility(View.INVISIBLE);
                }
                else if (position == 1){
                    circleIndicator.setVisibility(View.VISIBLE);
                    continueBtn.setVisibility(View.INVISIBLE);
                }
                else if (position == 2){
                    continueBtn.setVisibility(View.VISIBLE);
                    circleIndicator.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}