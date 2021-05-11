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

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    boolean mIsStateAlreadySaved = false;
    boolean mPendingShowDialog = false;

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
                showPopup(2);
                // once check in or check out update the status code in db.

                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showPopup(int statusCode) {
        if(mIsStateAlreadySaved){
            mPendingShowDialog = true;
        }else {
            DialogFragment dialog = new PopupDialogFragment();
            dialog.show(getSupportFragmentManager(), "Hi");
        }
    }

    @Override
    public void onResumeFragments(){
        super.onResumeFragments();
        mIsStateAlreadySaved = false;
        if(mPendingShowDialog){
            mPendingShowDialog = false;
            showPopup(3);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsStateAlreadySaved = true;
    }

}