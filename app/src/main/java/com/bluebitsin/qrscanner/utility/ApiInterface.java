package com.bluebitsin.qrscanner.utility;

import com.bluebitsin.qrscanner.model.QRScanResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("scan_qr_data.php")
    Call<QRScanResponse> getQRScanData();

    @POST("update_booking_status.php")
    @FormUrlEncoded
    Call<ResponseBody> updateBookingStatus(@Field("check_in_status") int checkStatus,
                                           @Field("booking_id") int bookingID,
                                           @Field("customer_id") int customerId,
                                           @Field("agent_id") int agentId,
                                           @Field("timestamp") long timestamp);
}


