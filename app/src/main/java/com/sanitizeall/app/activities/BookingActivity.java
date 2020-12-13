package com.sanitizeall.app.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sanitizeall.app.utils.AppConstant;
import com.sanitizeall.app.utils.AppPreference;
import com.sanitizeall.app.R;
import com.sanitizeall.app.utils.RetrofitClient;
import com.sanitizeall.app.utils.RetrofitService;
import com.sanitizeall.app.utils.Utility;
import com.sanitizeall.app.models.AddBookingResponse;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private TextInputEditText fullNameEt, emailEt, addressEt, sizeEt, placeTypeEt;

    private EditText mobileEt;

    private TextView dateEt;
    private MaterialButton registerButton;

    private RetrofitService retrofitService;

    private String mUserId = "";
    private String mFirebaseUserId = "";

    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        mUserId= AppPreference.getStringPreferences(this, AppConstant.PREF_USER_ID);
        mFirebaseUserId = AppPreference.getStringPreferences(this, AppConstant.PREF_FIREBASE_USER_ID);

        fullNameEt = findViewById(R.id.et_full_name);
        mobileEt = findViewById(R.id.et_mobile_number);
        emailEt = findViewById(R.id.et_email);
        addressEt = findViewById(R.id.et_address);
        sizeEt = findViewById(R.id.et_size);
        placeTypeEt = findViewById(R.id.et_place_type);
        dateEt = findViewById(R.id.et_date);

        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateEt);
            }
        });

        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = fullNameEt.getText().toString().trim();
                String mobile = mobileEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String size = sizeEt.getText().toString().trim();
                String placeType = placeTypeEt.getText().toString().trim();
                String date = dateEt.getText().toString().trim();

                boolean isValidForm = validateInputs();

                if (isValidForm){
                    register(fullName, mobile, email, address, size, placeType, date);
                }
            }
        });
    }

    private void register(String fullName, String mobile, String email, String address,
                          String size, String placeType, String date){
        progressDialog = Utility.createDialog(BookingActivity.this);
        retrofitService.addBooking(mFirebaseUserId, fullName, mobile, email, address, size, placeType, date, mUserId).enqueue(new Callback<AddBookingResponse>() {
            @Override
            public void onResponse(Call<AddBookingResponse> call, Response<AddBookingResponse> response) {
                if (response.isSuccessful()){
                    AddBookingResponse register = response.body();
                    Utility.cancelDialog(progressDialog);
                    if (register != null){
                        Integer status = register.getStatus();
                        if (status == 1){
                            showSuccessDialog();
                          //  Toast.makeText(BookingActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(BookingActivity.this, register.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddBookingResponse> call, Throwable t) {
                Utility.cancelDialog(progressDialog);
                Log.e("BOOKING ERROR : ",  t.getMessage());
                Toast.makeText(BookingActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(){
        if (fullNameEt.getText().toString().trim().equals("")){
            fullNameEt.setError(getString(R.string.error_empty));
            fullNameEt.requestFocus();
            return false;
        }
        else if (mobileEt.getText().toString().trim().equals("")){
            mobileEt.setError(getString(R.string.error_empty));
            mobileEt.requestFocus();
            return false;
        }
        else if (emailEt.getText().toString().trim().equals("")){
            emailEt.setError(getString(R.string.error_empty));
            emailEt.requestFocus();
            return false;
        }
        else if (addressEt.getText().toString().trim().equals("")){
            addressEt.setError(getString(R.string.error_empty));
            addressEt.requestFocus();
            return false;
        }
        else if (sizeEt.getText().toString().trim().equals("")){
            sizeEt.setError(getString(R.string.error_empty));
            sizeEt.requestFocus();
            return false;
        }
        else if (placeTypeEt.getText().toString().trim().equals("")){
            placeTypeEt.setError(getString(R.string.error_empty));
            placeTypeEt.requestFocus();
            return false;
        }
        else if (dateEt.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void showDatePickerDialog(final TextView textView){
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(year + "-" + (month + 1) +"-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showSuccessDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_booking_success);
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);

        TextView closeButton = dialog.findViewById(R.id.dialog_close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
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