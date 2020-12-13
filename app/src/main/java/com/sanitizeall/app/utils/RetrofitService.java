package com.sanitizeall.app.utils;

import com.sanitizeall.app.models.AddBookingResponse;
import com.sanitizeall.app.models.MyBookingResponse;
import com.sanitizeall.app.models.Register;
import com.sanitizeall.app.models.UpdateBookingResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("User")
    Call<Register> register(@Field("mobile") String mobileNumber);

    @FormUrlEncoded
    @POST("Booking")
    Call<AddBookingResponse> addBooking(@Field("id") String firebaseUserId,
                                        @Field("full_name") String fullname,
                                        @Field("mobile") String mobile,
                                        @Field("email") String email,
                                        @Field("address") String address,
                                        @Field("square_foot") String squareFoot,
                                        @Field("place_type") String placeType,
                                        @Field("date") String date,
                                        @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("UserBooking")
    Call<MyBookingResponse> getMyBookings(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("UserBooking")
    Call<MyBookingResponse> getAllBookings(@Field("user_id") String userId);

    @FormUrlEncoded
    @PUT("Booking")
    Call<UpdateBookingResponse> updateBooking(@Field("id") String id, @Field("status") String status);

}