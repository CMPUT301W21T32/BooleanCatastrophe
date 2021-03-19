package com.example.booleancatastrophe;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

// Extension of the application class to allow for the global variable "accountID"
// If the account fiel doesnt exist in memeory a new ID will be generated
public class ExperimentApplication extends Application {

    private String accountID;

    public String getAccountID(){
        return accountID;
    }

    public void  setAccountID(String accountID){
        this.accountID = accountID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadAccountID();
    }

    // Sets the global account ID variable from the accountID.txt file or generates one if it doesnt exist
    void loadAccountID(){
        UserManager uManager = new UserManager();
        File path = new File(this.getApplicationContext().getFilesDir(), "accountID.txt");
        //look for account file, if it exists set it equal to global accountID
        if(path.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(path));
                String ID = br.readLine();
                setAccountID(ID);
                uManager.getUser(ID, (user)->{ UserManager.currentUser = user; });
                Log.d("Experiment Application", "Found file and set global!");
            }
            catch(IOException e){
                Log.e("Experiment Application", "Reading ID file", e);
            }

        }
        // if not generate an account ID, add an account file, and assign it a user profile
        else{
            Log.d("Experiment Application", "File not found, creating");
                String aID = generateID();
                try{
                    FileOutputStream fos = openFileOutput("accountID.txt", Context.MODE_PRIVATE);
                    fos.write(aID.getBytes());
                    fos.close();
                }
                catch (IOException e){
                    Log.e("Experiment Application", "Writing ID file", e);
                }
                setAccountID(aID);
                uManager.addUser(new User(aID));
                uManager.getUser(aID, (user)->{ UserManager.currentUser = user; });
        }


    }

    // Function to generate a Pseudo-Unique ID, taken from https://www.pocketmagic.net/android-unique-device-id/
    private String generateID(){
        String uID = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ;
        return(uID);
    }




}
