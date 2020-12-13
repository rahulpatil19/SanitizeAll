package com.sanitizeall.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sanitizeall.app.R;
import com.sanitizeall.app.adapters.SlidingPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private CircleIndicator circleIndicator;
    private ViewPager viewPager;
    private SlidingPagerAdapter pagerAdapter;

    private static final Integer STATUS_RESULT_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        circleIndicator = findViewById(R.id.circle_indicator);
        viewPager = findViewById(R.id.main_view_pager);

        pagerAdapter = new SlidingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(viewPager);
    }

    public void register(View view) {
        Intent intent = new Intent(MainActivity.this, BookingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_settings){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, STATUS_RESULT_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STATUS_RESULT_CODE){
            if (resultCode == RESULT_OK){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}