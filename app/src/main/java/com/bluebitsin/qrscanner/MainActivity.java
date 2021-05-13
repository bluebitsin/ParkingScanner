package com.bluebitsin.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bluebitsin.qrscanner.model.QRScanResponse;
import com.bluebitsin.qrscanner.utility.ApiClient;
import com.bluebitsin.qrscanner.utility.ApiInterface;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PopupDialogFragment.CheckStatusListener{

    private ProgressDialog progressDialog;
    private Button btnScan;
    private boolean mIsStateAlreadySaved = false;
    private boolean mPendingShowDialog = false;
    private boolean isQRValid;
    private int scanQRStatus; // 1= check-in, 2 = checkout
    private String qrScanMessage;

    private int bookingId = 1;
    private int customerId = 3;
    private final int agentId = 906100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btnScan);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        scanTicket();

    }

    private void callQRScanData(){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<QRScanResponse> call = apiService.getQRScanData();
        call.enqueue(new Callback<QRScanResponse>() {
            @Override
            public void onResponse(Call<QRScanResponse> call, Response<QRScanResponse> response) {

                progressDialog.dismiss();

                if(response.body() != null){

                    QRScanResponse qrResponse = response.body();
                    Log.d(ParkingConstants.TAG_MAIN_ACTIVITY, qrResponse.toString());

                    // initialize popup dialog arguments
                    isQRValid = qrResponse.getQrData().getIsQrValid();
                    scanQRStatus = qrResponse.getQrData().getScanQrStatus();
                    qrScanMessage = qrResponse.getQrData().getScanStatusMessage();
                    bookingId = qrResponse.getQrData().getBookingId();

                    showPopup();
                }

            }

            @Override
            public void onFailure(Call<QRScanResponse>call, Throwable t) {
                // Log error here since request failed
                progressDialog.dismiss();
                Log.e(ParkingConstants.TAG_MAIN_ACTIVITY, t.toString());
            }
        });
    }


    public void scanTicket(){

        btnScan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan Parking Ticket");
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();

            }
        });
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                progressDialog.show();
                // make api call to check if qr is valid
                callQRScanData();
                // if valid then check is qr expired or not
                // if qr is ok show dialog with necessary details like car no, model, verified tag,
                // show buttons check in / check out depending on status code.
                // once check in or check out update the status code in db.

                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showPopup() {

        Bundle args = new Bundle();
        args.putBoolean(ParkingConstants.IS_QR_VALID, isQRValid);
        args.putInt(ParkingConstants.SCAN_QR_STATUS, scanQRStatus);
        args.putString(ParkingConstants.QR_STATUS_MESSAGE, qrScanMessage);

        if(mIsStateAlreadySaved){
            mPendingShowDialog = true;
        }else {

            DialogFragment dialog = new PopupDialogFragment();
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), ParkingConstants.TAG_SCAN_STATUS_DIALOG);
        }
    }

    @Override
    public void onResumeFragments(){
        super.onResumeFragments();
        mIsStateAlreadySaved = false;
        if(mPendingShowDialog){
            mPendingShowDialog = false;
            showPopup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsStateAlreadySaved = true;
    }

    @Override
    public void updateCheckStatus(int checkStatus) {

        long timestamp = System.currentTimeMillis();

        progressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.updateBookingStatus(checkStatus, bookingId,
                                                                customerId, agentId, timestamp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Response Code: "+ response.code()
                        , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}