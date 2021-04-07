package com.example.booleancatastrophe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.booleancatastrophe.model.CodeManager;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.Trial;
import com.google.firebase.firestore.GeoPoint;

public class GenerateQRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r_code);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.top_app_toolbar_generate_qr);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(view -> {
            finish();
        });


        // todo get real data from intent to pass tto qrcode generator
        Experiment experiment = generateTestExperiment();
        Trial trial = generateTestTrial();
        CodeManager codeManager = new CodeManager();
        Bitmap bitmap = codeManager.generateQrCode(experiment, trial, 250);
        ImageView imageView =  findViewById(R.id.qr_code_image_view);
        imageView.setImageBitmap(bitmap);

        Button printBtn = findViewById(R.id.btn_print);
        printBtn.setOnClickListener(view -> {
            PrintHelper photoPrinter = new PrintHelper(this);
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            photoPrinter.printBitmap("qrcode.bmp - print", bitmap);

        });

        // generate trial details fragment and add to the empty placeholder frame
        getSupportFragmentManager().beginTransaction()
                .add(R.id.trial_details_frame, TrialDetailsFragment.newInstance(experiment, trial))
                .commit();

    }

    private Trial generateTestTrial() {
        return new Trial("experimenter_name", 5.54, new GeoPoint(-90, -180), ExperimentType.MEASUREMENT);
    }

    private Experiment generateTestExperiment() {
        return new Experiment("This experiment is very cool", "Edmonton, AB", "experiment Owner", 20, ExperimentType.MEASUREMENT);
    }

}