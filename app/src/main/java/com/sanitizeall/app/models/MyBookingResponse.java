package com.sanitizeall.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyBookingResponse {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("login_by")
    @Expose
    private String loginType;

    @SerializedName("data")
    @Expose
    private List<MyBookingData> myBookingDataList;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMyBookingDataList(List<MyBookingData> myBookingDataList) {
        this.myBookingDataList = myBookingDataList;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<MyBookingData> getMyBookingDataList() {
        return myBookingDataList;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public static class MyBookingData{
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("firebase_token")
        @Expose
        private String firebaseToken;

        @SerializedName("full_name")
        @Expose
        private String fullName;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("mobile")
        @Expose
        private String mobile;

        @SerializedName("device_token")
        @Expose
        private String deviceToken;

        @SerializedName("is_verified")
        @Expose
        private String isVerified;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("square_foot")
        @Expose
        private String squareFoot;

        @SerializedName("place_type")
        @Expose
        private String placeType;

        @SerializedName("date")
        @Expose
        private String date;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("created_at")
        @Expose
        private String createAt;

        public void setId(String id) {
            this.id = id;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setFirebaseToken(String firebaseToken) {
            this.firebaseToken = firebaseToken;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setSquareFoot(String squareFoot) {
            this.squareFoot = squareFoot;
        }

        public void setPlaceType(String placeType) {
            this.placeType = placeType;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getFirebaseToken() {
            return firebaseToken;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public String getAddress() {
            return address;
        }

        public String getSquareFoot() {
            return squareFoot;
        }

        public String getPlaceType() {
            return placeType;
        }

        public String getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }

        public String getCreateAt() {
            return createAt;
        }
    }
}
