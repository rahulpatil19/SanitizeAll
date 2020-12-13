package com.sanitizeall.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitizeall.app.R;
import com.sanitizeall.app.models.MyBookingResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllBookingAdapter extends RecyclerView.Adapter<AllBookingAdapter.BookingViewHolder> {

    private Context context;
    private List<MyBookingResponse.MyBookingData> myBookingDataList;

    public AllBookingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_bookings, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        MyBookingResponse.MyBookingData bookingData = myBookingDataList.get(position);

        String fullName = bookingData.getFullName();
        String date = bookingData.getDate();
        String mobile = bookingData.getMobile();
        String email = bookingData.getEmail();
        String address = bookingData.getAddress();
        String buildupArea = bookingData.getSquareFoot();
        String placeType = bookingData.getPlaceType();

        holder.nameTv.setText(fullName);
        holder.dateTv.setText(date);
        holder.mobileTv.setText(mobile);
        holder.emailTv.setText(email);
        holder.addressTv.setText(address);
        holder.buildupAreaTv.setText("Buildup Area - " + buildupArea);
        holder.placeTypeTv.setText("Place Type - " + placeType);
    }

    @Override
    public int getItemCount() {
        return null == myBookingDataList ? 0 : myBookingDataList.size();
    }

    public void addBookingList(List<MyBookingResponse.MyBookingData> myBookingDataList){
        this.myBookingDataList = myBookingDataList;
        notifyDataSetChanged();
    }

     class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView mobileTv;
        private TextView dateTv;
        private TextView emailTv;
        private TextView addressTv;
        private TextView buildupAreaTv;
        private TextView placeTypeTv;

        private BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.booking_fullname_tv);
            mobileTv = itemView.findViewById(R.id.booking_mobile_tv);
            dateTv = itemView.findViewById(R.id.booking_date_tv);
            emailTv = itemView.findViewById(R.id.booking_email_tv);
            addressTv = itemView.findViewById(R.id.booking_address_tv);
            buildupAreaTv = itemView.findViewById(R.id.booking_buildup_area_tv);
            placeTypeTv = itemView.findViewById(R.id.booking_place_type_tv);
        }
    }
}
