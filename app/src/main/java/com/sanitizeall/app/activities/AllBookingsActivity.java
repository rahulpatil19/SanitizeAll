package com.sanitizeall.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sanitizeall.app.R;
import com.sanitizeall.app.adapters.AllBookingAdapter;
import com.sanitizeall.app.models.MyBookingResponse;
import com.sanitizeall.app.utils.AppConstant;
import com.sanitizeall.app.utils.AppPreference;
import com.sanitizeall.app.utils.RetrofitClient;
import com.sanitizeall.app.utils.RetrofitService;

import java.util.List;

public class AllBookingsActivity extends AppCompatActivity {

    private RetrofitService retrofitService;

    private ProgressBar progressBar;

    private AllBookingAdapter allBookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        RecyclerView recyclerView = findViewById(R.id.all_bookings_recyclerview);
        progressBar = findViewById(R.id.progress_bar);

        allBookingAdapter = new AllBookingAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(allBookingAdapter);

        String userId = AppPreference.getStringPreferences(this, AppConstant.PREF_USER_ID);

        getAllBookings(userId);
    }

    private void getAllBookings(String userId){
        progressBar.setVisibility(View.VISIBLE);
        retrofitService.getAllBookings(userId).enqueue(new Callback<MyBookingResponse>() {
            @Override
            public void onResponse(Call<MyBookingResponse> call, Response<MyBookingResponse> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    MyBookingResponse myBookingResponse = response.body();

                    if (myBookingResponse != null){
                        Integer status = myBookingResponse.getStatus();
                        if (status == 1){
                            List<MyBookingResponse.MyBookingData> myBookingDataList = myBookingResponse.getMyBookingDataList();
                            if (myBookingDataList.size() > 0){
                                allBookingAdapter.addBookingList(myBookingDataList);
                            }
                            else {
                                Toast.makeText(AllBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AllBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AllBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AllBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyBookingResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AllBookingsActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}