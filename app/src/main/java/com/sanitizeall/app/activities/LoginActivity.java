package com.sanitizeall.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sanitizeall.app.models.Register;
import com.sanitizeall.app.utils.AppConstant;
import com.sanitizeall.app.utils.AppPreference;
import com.sanitizeall.app.R;
import com.sanitizeall.app.utils.RetrofitClient;
import com.sanitizeall.app.utils.RetrofitService;
import com.sanitizeall.app.utils.Utility;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView buttonLogin;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;

    private EditText mobileNumberEt;

    private RetrofitService retrofitService;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        buttonLogin = findViewById(R.id.button_login);

        mobileNumberEt = findViewById(R.id.mobile_number_et);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = mobileNumberEt.getText().toString().trim();

                if (mobileNumber.length() == 10){
                    verifyPhoneNumber("+91" + mobileNumber);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                Toast.makeText(LoginActivity.this, "Verification in process", Toast.LENGTH_SHORT).show();

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Utility.cancelDialog(progressDialog);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                Utility.cancelDialog(progressDialog);
                // Save verification ID and resending token so we can use them later
                //mResendToken = token;

                Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                intent.putExtra("verification_id", verificationId);
                startActivity(intent);
                finish();
            }
        };
    }

    private void verifyPhoneNumber(String phoneNumber){
        progressDialog = Utility.createDialog(LoginActivity.this);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            Utility.cancelDialog(progressDialog);
                            FirebaseUser user = task.getResult().getUser();
                            String uId = user.getUid();
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String number = user.getPhoneNumber();

                            Toast.makeText(LoginActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();

                            submitUserData(uId, name, email, number);

                           /* AppPreference.setBooleanPreferences(LoginActivity.this, AppConstant.LOGIN_STATUS, true);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_USER_ID, uId);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_USERNAME, name);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_EMAIL, email);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_PHONE_NUMBER, number);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
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

                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_USER_ID, userId);

                            AppPreference.setBooleanPreferences(LoginActivity.this, AppConstant.LOGIN_STATUS, true);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_FIREBASE_USER_ID, firebaseUserId);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_USERNAME, name);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_EMAIL, email);
                            AppPreference.setStringPreferences(LoginActivity.this, AppConstant.PREF_PHONE_NUMBER, mobile);

                            Utility.cancelDialog(progressDialog);

                            Toast.makeText(LoginActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Utility.cancelDialog(progressDialog);
                Toast.makeText(LoginActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}