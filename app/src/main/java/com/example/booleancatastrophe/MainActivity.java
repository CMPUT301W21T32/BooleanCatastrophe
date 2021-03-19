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
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main Activity";

    Toolbar topAppToolbar;
    //ActionBar topAppActionbar;
    TabLayout tabOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ExperimentManager e = new ExperimentManager();
        //e.addExperiment(new Experiment("Coin", "AB", ((ExperimentApplication) this.getApplication()).getAccountID(), 5, ExperimentType.BINOMIAL));

        /* Set up the top toolbar - listeners are set up for different toolbar button actions */
        topAppToolbar = (Toolbar) findViewById(R.id.top_app_toolbar);
        setSupportActionBar(topAppToolbar);    // display the toolbar
//      topAppActionbar = getSupportActionBar();    // Specific actions on the toolbar
        /* Listener for clicking on the navigation (far left) menu-style button */
        topAppToolbar.setNavigationOnClickListener(v -> {
            // handle navigation icon (far left) click
            topAppToolbar.setTitle("NAV PRESS");
        });
        /* Listener for clicking on the various action buttons set up on the toolbar */
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

        /* Set up the tabs and detect when different ones are selected */
        tabOptions = (TabLayout) findViewById(R.id.tabs);
        tabOptions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int id = tab.getPosition();
                if(id == 0) {
                    topAppToolbar.setTitle("HOME");
                } else if(id == 1) {
                    topAppToolbar.setTitle("OWNED TAB");
                } else if(id == 2) {
                    topAppToolbar.setTitle("SUBSCRIBED TAB");
                } else if(id == 3) {
                    topAppToolbar.setTitle("ACTIVE TAB");
                } else if(id == 4) {
                    topAppToolbar.setTitle("ENDED TAB");
                } else {
                    // default case - should always be home?
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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