package com.sanitizeall.app.utils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;

import com.sanitizeall.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static boolean isNetAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }
        }else {
            return false;
        }
        return false;
    }

    public static Dialog createDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public static void cancelDialog(Dialog dialog){
        if (dialog != null){
            dialog.dismiss();
        }
    }

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getTimeDifference(String startTime, String endTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
        String totalTime = null;
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            totalTime = String.valueOf(end.getTime() - start.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalTime;
    }

    public static String formatDate(String messageDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = dateFormat.format(messageDate);
        return time;
    }

    public static String getDate(String date){
        SimpleDateFormat inputPattern =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputPattern = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date inputDate;
        String outputDate = null;
        try {
            inputDate = inputPattern.parse(date);
            outputDate = outputPattern.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    public static boolean validatePanNumber(String panNumber){
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panNumber);
        if (matcher.matches()) {
            return true;
        }
        else {
            return false;
        }
    }
}