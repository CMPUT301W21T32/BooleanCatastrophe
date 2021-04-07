package com.example.booleancatastrophe.model;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class CodeManager {

    private static final String TAG = "Code Manager";
    private static final String experimentTag = "ExperimentID";
    private static final String trialTypeTag = "TrialType";
    private static final String trialResultTag = "TrialResult";
    private static final String trialLocationTag = "TrialLocation";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = db.collection("codes");


    public Bitmap generateQrCode(Experiment experiment, Trial trial, int size){
        QRCodeWriter writer = new QRCodeWriter();
        String data = experimentTag + ": " + experiment.getId() + ", "
                        + trialTypeTag + ": " + trial.getType().toString() + ", "
                        + trialResultTag + ": " + trial.getResult().toString() + ", "
                        + trialLocationTag + ": " + trial.getLocation().toString();
        BitMatrix bitMatrix;
        try {
            bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap imageBitmap = Bitmap.createBitmap (size, size, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j ++) {
                imageBitmap.setPixel (i, j, bitMatrix.get (i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        return imageBitmap;
    }
}
