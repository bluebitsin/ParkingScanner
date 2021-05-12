package com.bluebitsin.qrscanner.utility;

import com.bluebitsin.qrscanner.model.QRScanResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("scan_qr_data.php")
    Call<QRScanResponse> getQRScanData();

}
