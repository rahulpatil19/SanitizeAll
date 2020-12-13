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
import com.sanitizeall.app.adapters.MyBookingAdapter;
import com.sanitizeall.app.models.AddBookingResponse;
import com.sanitizeall.app.models.MyBookingResponse;
import com.sanitizeall.app.models.UpdateBookingResponse;
import com.sanitizeall.app.utils.AppConstant;
import com.sanitizeall.app.utils.AppPreference;
import com.sanitizeall.app.utils.RetrofitClient;
import com.sanitizeall.app.utils.RetrofitService;

import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private RetrofitService retrofitService;

    private ProgressBar progressBar;

    private MyBookingAdapter myBookingAdapter;

    private String mUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        RecyclerView recyclerView = findViewById(R.id.my_bookings_recyclerview);
        progressBar = findViewById(R.id.progress_bar);

        myBookingAdapter = new MyBookingAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myBookingAdapter);

        mUserId = AppPreference.getStringPreferences(this, AppConstant.PREF_USER_ID);

        getMyBookings(mUserId);
    }

    private void getMyBookings(String userId){
        progressBar.setVisibility(View.VISIBLE);
        retrofitService.getMyBookings(userId).enqueue(new Callback<MyBookingResponse>() {
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
                                myBookingAdapter.addBookingDataList(myBookingDataList);
                            }
                            else {
                                Toast.makeText(MyBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MyBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MyBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MyBookingsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyBookingResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MyBookingsActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
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


   /* private void cancelBooking(int position, MyBookingResponse.MyBookingData myBookingData){
        retrofitService.updateBooking(mUserId, "1").enqueue(new Callback<UpdateBookingResponse>() {
            @Override
            public void onResponse(Call<UpdateBookingResponse> call, Response<UpdateBookingResponse> response) {
                if (response.isSuccessful()){
                    UpdateBookingResponse  updateBookingResponse = response.body();
                    progressBar.setVisibility(View.GONE);
                    if (updateBookingResponse != null){
                        int status = updateBookingResponse.getStatus();
                        if (status == 1){
                            Toast.makeText(MyBookingsActivity.this, "Booking successfully canceled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBookingResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyBookingsActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}