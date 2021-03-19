package com.example.booleancatastrophe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentManager;
import com.example.booleancatastrophe.model.ExperimentType;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ExperimentManager e = new ExperimentManager();
        //e.addExperiment(new Experiment("Coin", "AB", ((ExperimentApplication) this.getApplication()).getAccountID(), 5, ExperimentType.BINOMIAL));

        /* Set up the top toolbar - listeners are set up for different toolbar button actions */
        Toolbar topAppToolbar = (Toolbar) findViewById(R.id.top_app_toolbar);
        setSupportActionBar(topAppToolbar);    // display the toolbar
//        ActionBar topAppActionbar = getSupportActionBar();    // Specific actions on the toolbar

        /* Listener for clicking on the navigation (far left) menu-style button */
        topAppToolbar.setNavigationOnClickListener(v -> {
            // handle navigation icon (far left) click
            topAppToolbar.setTitle("NAV PRESS");
        });

        topAppToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.top_app_bar_search) {    // User selected the top bar search icon
                // TODO add behaviour of search, may want to look up SearchView for better behaviour option
                topAppToolbar.setTitle("SEARCH PRESS");
                return true;
            } else if(id == R.id.top_app_bar_userprofile) {    // User selected the top bar profile icon
                // TODO launch the user profile details activity where they can edit their details, etc
                topAppToolbar.setTitle("PROFILE PRESS");
                return true;
            } else {   // User's action not recognized; interacts with 'more' dropdown (look into)!
                topAppToolbar.setTitle("IDK");
                return false;
            }
        });

        

    }

    /* Necessary to create top bar icons */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }
}