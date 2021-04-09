package com.example.booleancatastrophe;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;


public class TabHomeFragment extends Fragment {

    View view;
    Button btnAddExperiment;
    Button btnViewProfile;
    Button btnSearch;
    Button btnScanQR;
    Button btnScanBarcode;

    public TabHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_home, container,
                false);

        btnAddExperiment = view.findViewById(R.id.btn_home_addexperiment);
        btnViewProfile = view.findViewById(R.id.btn_home_viewprofile);
        btnSearch = view.findViewById(R.id.btn_home_search);
        btnScanQR = view.findViewById(R.id.btn_home_scan_qr);
        btnScanBarcode = view.findViewById(R.id.btn_home_scan_barcode);

        btnAddExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishExperimentFragment newExpFrag = new PublishExperimentFragment();
                newExpFrag.show(getFragmentManager(), "PUBLISH_EXPERIMENT");
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO launch search activity
            }
        });

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(getActivity()).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE).initiateScan();
            }
        });

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(getActivity()).setDesiredBarcodeFormats(IntentIntegrator.EAN_13).initiateScan();
            }
        });

        return view;
    }
}