package com.sanitizeall.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sanitizeall.app.utils.AppConstant;
import com.sanitizeall.app.utils.AppPreference;
import com.sanitizeall.app.R;
import com.sanitizeall.app.utils.RetrofitClient;
import com.sanitizeall.app.utils.RetrofitService;
import com.sanitizeall.app.utils.Utility;
import com.sanitizeall.app.models.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private static final String TAG = VerificationActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    private String mVerificationId = "";
    private String mVerificationCode = "";

    private MaterialButton verifyButton;

    private EditText otpEt;

    private Dialog progressDialog;

    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verifyButton = findViewById(R.id.button_otp_verify);

        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        otpEt = findViewById(R.id.otp_et);
        mAuth = FirebaseAuth.getInstance();

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        Intent intent = getIntent();
        if (intent != null) {
            mVerificationId = intent.getExtras().getString("verification_id");
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVerificationCode = otpEt.getText().toString().trim();
                    if (mVerificationCode.length() > 0){
                        final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mVerificationCode);
                        progressDialog = Utility.createDialog(VerificationActivity.this);
                        signInWithPhoneAuthCredential(credential);
                    }
                    else {
                        Toast.makeText(VerificationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            String uId = user.getUid();
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String mobile = user.getPhoneNumber();

                            Toast.makeText(VerificationActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();

                            submitUserData(uId, name, email, mobile);

                        } else {
                            Utility.cancelDialog(progressDialog);
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerificationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void submitUserData(final String firebaseUserId, final String name, final String email, final String mobile){
        retrofitService.register(mobile).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.isSuccessful()){
                    Register register = response.body();
                    if (register != null){
                        if (register.getStatus() == 1 || register.getStatus() == 2){

                            String userId = register.getData();

                            AppPreference.setStringPreferences(VerificationActivity.this, AppConstant.PREF_USER_ID, userId);

                            AppPreference.setBooleanPreferences(VerificationActivity.this, AppConstant.LOGIN_STATUS, true);
                            AppPreference.setStringPreferences(VerificationActivity.this, AppConstant.PREF_FIREBASE_USER_ID, firebaseUserId);
                            AppPreference.setStringPreferences(VerificationActivity.this, AppConstant.PREF_USERNAME, name);
                            AppPreference.setStringPreferences(VerificationActivity.this, AppConstant.PREF_EMAIL, email);
                            AppPreference.setStringPreferences(VerificationActivity.this, AppConstant.PREF_PHONE_NUMBER, mobile);

                            Utility.cancelDialog(progressDialog);

                            Toast.makeText(VerificationActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(VerificationActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Utility.cancelDialog(progressDialog);
                Toast.makeText(VerificationActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}