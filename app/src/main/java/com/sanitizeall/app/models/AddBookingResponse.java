package com.sanitizeall.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBookingResponse {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
