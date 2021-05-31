package com.bluebitsin.qrscanner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bluebitsin.qrscanner.model.QRScanResponse;
import com.bluebitsin.qrscanner.model.QrData;
import com.bluebitsin.qrscanner.model.RequestCheckStatus;
import com.bluebitsin.qrscanner.model.User;
import com.bluebitsin.qrscanner.utility.ApiClient;
import com.bluebitsin.qrscanner.utility.ApiInterface;
import com.bluebitsin.qrscanner.utility.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PopupDialogFragment.CheckStatusListener {

    private ProgressDialog progressDialog;
    private boolean mIsStateAlreadySaved = false;
    private boolean mPendingShowDialog = false;
    private boolean isQRValid;
    private int scanQRStatus; // 1= check-in, 2 = checkout
    private String qrScanMessage;
    private String vehicleModel;
    private String vehicleNumber;
    private String parkingBookDate;

    private LinearLayout btnScan, btnCloseGate, btnFeedback, btnLogout;

    private int bookingId;
    private String reservationId = null;
    private User loginAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        scanTicket();
        closeGate();
        sendFeedback();
        logout();

    }

    private void initViews() {

        btnScan = findViewById(R.id.btnScan);
        btnCloseGate = findViewById(R.id.btnCloseGate);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        //new views
        TextView txtAgentName = findViewById(R.id.txtAgentName);
        TextView txtAgentId = findViewById(R.id.txtAgentId);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        loginAgent = (User) getIntent().getSerializableExtra(ParkingConstants.OBJECT_USER);
        Log.v(ParkingConstants.TAG_MAIN_ACTIVITY, loginAgent.toString());

        txtAgentName.setText(loginAgent.getUserName());
        txtAgentId.setText(loginAgent.getAgentCode());

    }
    private void logout() {

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void sendFeedback() {

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Send Feedback", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void closeGate() {

        btnCloseGate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gateStatusUpdate(0);
            }
        });
    }
    public void scanTicket() {

        btnScan.setOnClickListener(new View.OnClickListener() {

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
    private void showPopup() {

        Bundle args = new Bundle();
        args.putBoolean(ParkingConstants.IS_QR_VALID, isQRValid);
        args.putInt(ParkingConstants.SCAN_QR_STATUS, scanQRStatus);
        args.putString(ParkingConstants.QR_STATUS_MESSAGE, qrScanMessage);
        args.putString(ParkingConstants.VEHICLE_MODEL, vehicleModel);
        args.putString(ParkingConstants.VEHICLE_NUMBER, vehicleNumber);
        args.putString(ParkingConstants.PARKING_BOOK_DATE, parkingBookDate);

        if (mIsStateAlreadySaved) {
            mPendingShowDialog = true;
        } else {

            DialogFragment dialog = new PopupDialogFragment();
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), ParkingConstants.TAG_SCAN_STATUS_DIALOG);
        }
    }


    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                //progressDialog.show();
                reservationId = result.getContents();
                Log.v(ParkingConstants.TAG_MAIN_ACTIVITY, reservationId);

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
    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        mIsStateAlreadySaved = false;
        if (mPendingShowDialog) {
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
    public void onBackPressed() {

        // show logout alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Parking Scanner")
                .setMessage("Are you sure you want to exit?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        //super.onBackPressed();
    }


    /**
     *  API CALL - verify qr data
     */
    private void callQRScanData() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<QRScanResponse> call = apiService.getQRScanData(reservationId);
        call.enqueue(new Callback<QRScanResponse>() {
            @Override
            public void onResponse(Call<QRScanResponse> call, Response<QRScanResponse> response) {

                progressDialog.dismiss();
                QRScanResponse qrResponse = response.body();

                if (qrResponse != null && qrResponse.getResponseStatus()) {

                    QrData qrData = qrResponse.getQrData();
                    Log.d(ParkingConstants.TAG_MAIN_ACTIVITY, qrResponse.toString());

                    // initialize popup dialog arguments
                    isQRValid = qrResponse.getQrData().getIsQrValid();
                    scanQRStatus = qrResponse.getQrData().getScanQrStatus();
                    qrScanMessage = qrResponse.getQrData().getScanStatusMessage();
                    bookingId = qrResponse.getQrData().getBookingId();
                    vehicleModel = qrData.getCarModel();
                    vehicleNumber = qrData.getCarNo();
                    parkingBookDate = qrData.getBookingTimestamp().toString();

                    showPopup();

                }else{

                    // show invalid QR message
                    showInvalidQRMessage();

                }

            }

            @Override
            public void onFailure(Call<QRScanResponse> call, Throwable t) {
                // Log error here since request failed
                progressDialog.dismiss();
                Log.e(ParkingConstants.TAG_MAIN_ACTIVITY, t.toString());
            }
        });
    }
    private void showInvalidQRMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Ticket Scan")
                .setMessage("Either QR Code scanned is invalid or ticket get expired.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * API CALL - Close Gate
     * @param gateStatus
     */
    private void gateStatusUpdate(int gateStatus) {

        progressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.updateGateStatus(gateStatus);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();

                if(response.code() == 200){

                    Toast.makeText(MainActivity.this, "Gate Closed"
                            , Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MainActivity.this, "Error while closing gate"
                            , Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * API CALL - update check status
     * @param checkStatus
     */
    @Override
    public void updateCheckStatus(int checkStatus) {

        // Check-in or Checkout
        Log.d("Check Status: ", checkStatus+", "+Helper.getDateTime());

        RequestCheckStatus requestCheckStatus = new RequestCheckStatus();
        requestCheckStatus.setAgentId(loginAgent.getAgentCode());
        requestCheckStatus.setCheckStatus(checkStatus);
        requestCheckStatus.setCustomerId(0);
        requestCheckStatus.setReservationId(reservationId);
        requestCheckStatus.setTimestamp(new Date());
        requestCheckStatus.setBookingId(bookingId);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String json = gson.toJson(requestCheckStatus);
        Log.d("MUKESH JSON", json);

        progressDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.updateBookingStatus(requestCheckStatus);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressDialog.dismiss();
                Log.d("Response Code: ", response.code()+"");
                if(response.code() == 200){
                    Toast.makeText(MainActivity.this, "OK"
                            , Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(MainActivity.this, "ERROR"
                            , Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}