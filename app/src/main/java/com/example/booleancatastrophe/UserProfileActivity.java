package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.booleancatastrophe.model.User;
import com.example.booleancatastrophe.model.UserManager;

// TODO fix it up so it properly interacts with User/UserManager to update the name and email database fields

public class UserProfileActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etEmail;
    Button btnSave;
    UserManager userManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user = UserManager.currentUser;
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
            userManager.setUsername(user.getUsername(), etUsername.getText().toString());
            userManager.setEmail(user.getUsername(), etEmail.getText().toString());
            finish();
        });
    }
}