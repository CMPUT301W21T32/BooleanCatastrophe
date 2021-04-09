package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;


/**
 * This activity is called to view other user's profiles in a non-editable way or your own profile
 * with the fields editable so you can change your username or contact email
 **/
public class UserProfileActivity extends AppCompatActivity {

    private final static String TAG = "User Profile Activity";

    EditText etUsername;
    EditText etEmail;
    Button btnSave;
    UserManager userManager = new UserManager();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Get the passed User through the intent
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (User) getIntent().getSerializableExtra("user");
        }

        if(user == null){
            finish();
        }

        etUsername = (EditText) findViewById(R.id.et_user_name);
        etEmail = (EditText) findViewById(R.id.et_user_email);
        btnSave = (Button) findViewById(R.id.btn_save_profile);

        /* Set up UI-side data validation (potentially add more filters here) */
        etUsername.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(User.MAX_USERNAME_LENGTH)});
        etEmail.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(User.MAX_EMAIL_LENGTH)});

        /* Display the user's current username and email, which can be modified */
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());

        /* Save Profile button onclick listener; update the user's username and email and
        * return to the MainActivity (for now) */
        btnSave.setOnClickListener(v -> {
            userManager.setUsername(user.getAccountID(), etUsername.getText().toString(), (Void result) -> {
                    //Update the global user on successful call back, ideally done in the manager but I couldn't get it working
                    ((ExperimentApplication) getApplication()).setCurrentUsername(etUsername.getText().toString());
            });
            userManager.setEmail(user.getAccountID(), etEmail.getText().toString(), (Void result) -> {
                    ((ExperimentApplication) getApplication()).setCurrentUserEmail(etEmail.getText().toString());
            });
            finish();
        });
    }
}