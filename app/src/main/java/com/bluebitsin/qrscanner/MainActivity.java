package com.bluebitsin.qrscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements PopupDialogFragment.CheckStatusListener{

    private Button btnScan;
    private boolean mIsStateAlreadySaved = false;
    private boolean mPendingShowDialog = false;
    private boolean isQRValid = true;
    private int scanQRStatus = 2; // 1= check-in, 2 = checkout
    private String qrScanMessage = "Car = Swift Desire, No = UP3250101";
    //private String qrScanMessage = "QR Code might be expired or already used or wrong QR Code scanned. Please scan correct QR Code.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = findViewById(R.id.btnScan);
        scanTicket();

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

                // make api call to check if qr is valid
                // if valid then check is qr expired or not
                // if qr is ok show dialog with necessary details like car no, model, verified tag,
                // show buttons check in / check out depending on status code.
                showPopup();
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
        Toast.makeText(this, "Status Code "+checkStatus+" is called.", Toast.LENGTH_SHORT).show();
    }
}