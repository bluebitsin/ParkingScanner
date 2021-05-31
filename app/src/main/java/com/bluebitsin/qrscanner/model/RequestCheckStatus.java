package com.bluebitsin.qrscanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RequestCheckStatus {

    @SerializedName("check_status")
    @Expose
    private int checkStatus;

    @SerializedName("agent_id")
    @Expose
    private String agentId;

    @SerializedName("timestamp")
    @Expose
    private Date timestamp;

    @SerializedName("booking_id")
    @Expose
    private int bookingId;

    @SerializedName("customer_id")
    @Expose
    private int customerId;

    @SerializedName("reservation_id")
    @Expose
    private String reservationId;

    public RequestCheckStatus() {
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "RequestCheckStatus{" +
                "checkStatus=" + checkStatus +
                ", agentId='" + agentId + '\'' +
                ", timestamp=" + timestamp +
                ", customerId=" + customerId +
                ", reservationId=" + reservationId +
                '}';
    }
}
