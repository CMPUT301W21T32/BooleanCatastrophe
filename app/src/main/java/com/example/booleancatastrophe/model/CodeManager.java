package com.example.booleancatastrophe.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.booleancatastrophe.ExperimentApplication;
import com.example.booleancatastrophe.interfaces.FirestoreCodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.RegEx;

public class CodeManager {

    private static final String TAG = "Code Manager";
    private static final String experimentTag = "ExperimentID";
    private static final String trialTypeTag = "TrialType";
    private static final String trialResultTag = "TrialResult";
    private static final String trialLocationTag = "TrialLocation";
    private static final String trialDateTag = "TrialDate";
    private static final String delimiter = "; ";
    private static final String colon = ": ";
    private static final Pattern regexPattern = Pattern.compile(experimentTag + colon + "(.*)" + delimiter
            + trialTypeTag + colon + "(.*)" + delimiter
            + trialResultTag + colon + "(.*)" + delimiter
            + trialLocationTag + colon + "(null|GeoPoint \\{ latitude=(-?[0-9]*.[0-9]*), longitude=(-?[0-9]*.[0-9]) \\})"
            + delimiter + trialDateTag + colon + "(.*)" + delimiter );

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference usersRef = db.collection("users");


    public static Bitmap generateQrCode(Experiment experiment, Trial trial, int size){
        QRCodeWriter writer = new QRCodeWriter();
        String data = experimentTag + colon + experiment.getId() + delimiter
                        + trialTypeTag + colon + trial.getType().toString() + delimiter
                        + trialResultTag + colon + trial.getResult().toString() + delimiter
                        + trialLocationTag + colon + (trial.getLocation()!=null ? trial.getLocation().toString() : "null") + delimiter
                        + trialDateTag + colon + trial.getDate().getTime() + delimiter;
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

    public static Pair<String, Trial> parseQrString(String qrString, String experimenterId) {
        Matcher m = regexPattern.matcher(qrString);
        Trial trial;
        if (m.find()) {
            String experimentId = m.group(1);
            String trialType = m.group(2);
            double trialResult = Double.parseDouble(Objects.requireNonNull(m.group(3)));
            double lat = Double.parseDouble(Objects.requireNonNull(m.group(5)));
            double lon = Double.parseDouble(Objects.requireNonNull(m.group(6)));
            long date = Long.parseLong(m.group(7));
            GeoPoint location = new GeoPoint(lat, lon);
            assert trialType != null;
            trial = new Trial(experimenterId, trialResult, location, ExperimentType.valueOf(trialType.toUpperCase()), new Date(date));
            return new Pair<>(experimentId, trial);
        } else {
            Log.d(TAG, "Could not parse QR code string");
        }
        return null;
    }

    public static void addBarcode(String accountId, String codeString, String experimentId, Trial trial) {
        usersRef.document(accountId).collection("barcodes")
                .document(codeString)
                .set(new Code(codeString, experimentId, trial));
    }

    public static void getBarcode(String accountId, String codeString, FirestoreCodeCallback firestoreCallback) {
        usersRef.document(accountId).collection("barcodes")
                .document(codeString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Found document");
                                firestoreCallback.OnCallBack(document.toObject(Code.class));
                            } else {
                                Log.d(TAG, "No such document");
                                firestoreCallback.OnCallBack(null);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
    }
}
