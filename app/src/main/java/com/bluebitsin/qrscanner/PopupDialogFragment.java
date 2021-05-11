package com.bluebitsin.qrscanner;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class PopupDialogFragment extends DialogFragment {

    private CheckStatusListener chekStatusListener;
    private ImageView iconValid;
    private TextView textHeading;
    private TextView textMessage;
    private Button btnCheckIn, btnCheckOut, btnOk;

    public interface CheckStatusListener{
        public void updateCheckStatus(int checkStatus);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            chekStatusListener = (CheckStatusListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(MainActivity.class.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public PopupDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PopupDialogFragment newInstance(String title) {
        PopupDialogFragment frag = new PopupDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_popup, null);
        //initialize views
        iconValid = (ImageView) view.findViewById(R.id.iconStatus);
        textHeading = (TextView) view.findViewById(R.id.statusMsg);
        textMessage = (TextView) view.findViewById(R.id.textTicketDetails);
        btnCheckIn = (Button) view.findViewById(R.id.btnCheckIn);
        btnCheckOut = (Button) view.findViewById(R.id.btnCheckOut);
        btnOk = (Button) view.findViewById(R.id.btnOK);

        // update views
        if(args.getBoolean(ParkingConstants.IS_QR_VALID)){
            iconValid.setImageResource(R.drawable.ic_valid);
            textHeading.setText("Valid Parking Ticket");
            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckOut.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
            //enable or disable on basis of scanQRStatus
            if(args.getInt(ParkingConstants.SCAN_QR_STATUS) == 1){
                btnCheckIn.setEnabled(true);
                btnCheckOut.setEnabled(false);
            }else{

                btnCheckIn.setEnabled(false);
                btnCheckOut.setEnabled(true);
            }

        }else{
            iconValid.setImageResource(R.drawable.ic_invalid);
            textHeading.setText("Invalid Parking Ticket");
            btnCheckIn.setVisibility(View.GONE);
            btnCheckOut.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
        }

        textMessage.setText(args.getString(ParkingConstants.QR_STATUS_MESSAGE));

        //add click listener
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chekStatusListener != null){

                    chekStatusListener.updateCheckStatus(ParkingConstants.CHECK_IN);

                }
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chekStatusListener != null){

                    chekStatusListener.updateCheckStatus(ParkingConstants.CHECK_OUT);

                }

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }
}
