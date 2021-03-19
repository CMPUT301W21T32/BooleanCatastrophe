package com.example.booleancatastrophe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


// Took a lot of help from https://stackoverflow.com/questions/41504539/android-tablayout-navigation-with-recyclerview-items
// for using recycler view and setting it up to work in a multi-tab style with appropriate fragments

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main Activity";

    Toolbar topAppToolbar;
    //ActionBar topAppActionbar;

    TabLayout tabLayout;
    ViewPager viewPager;
    CustomViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ExperimentManager e = new ExperimentManager();
        //e.addExperiment(new Experiment("Coin", "AB", ((ExperimentApplication) this.getApplication()).getAccountID(), 5, ExperimentType.BINOMIAL));

        tabLayout = (TabLayout) findViewById(R.id.tabs) ;
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager(), 1);

        // Add Fragments
        viewPagerAdapter.AddFragment(new TabHomeFragment(),
                "Home");
        viewPagerAdapter.AddFragment(new TabOwnedExperimentsFragment(),
                "Owned Experiments");
        viewPagerAdapter.AddFragment(new TabSubscribedExperimentsFragment(),
                "Subscribed Experiments");
        viewPagerAdapter.AddFragment(new TabActiveExperimentsFragment(),
                "Active Experiments");
        viewPagerAdapter.AddFragment(new TabEndedExperimentsFragment(),
                "Ended Experiments");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // SET TAB ICONS HERE LATER IF DESIRED, something along these lines:
        //tabLayout.getTabAt(0).setIcon(R.drawable)...

        /* Set up the top toolbar - listeners are set up for different toolbar button actions */
        topAppToolbar = (Toolbar) findViewById(R.id.top_app_toolbar);
        setSupportActionBar(topAppToolbar);    // display the toolbar
//      topAppActionbar = getSupportActionBar();    // Specific actions on the toolbar
        /* Listener for clicking on the navigation (far left) menu-style button */
        topAppToolbar.setNavigationOnClickListener(v -> {
            // handle navigation icon (far left) click
            topAppToolbar.setTitle("NAV PRESS");
        });

        // TODO renovate the toolbar and move these listeners outisde to an onOptionsItemSelected override function??

        /* Listener for clicking on the various action buttons set up on the toolbar */
        topAppToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.top_app_bar_search) {    // User selected the top bar search icon
                // TODO add behaviour of search, may want to look up SearchView for better behaviour option
                topAppToolbar.setTitle("SEARCH PRESS");
                return true;
            } else if(id == R.id.top_app_bar_userprofile) {    // User selected the top bar profile icon
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            } else {   // User's action not recognized; interacts with 'more' dropdown (look into)!
                topAppToolbar.setTitle("IDK");
                return false;
            }
        });


        /* Set up the floating action button and its listener */
        // TODO add new experiment fragment functionality
        FloatingActionButton btnAddExperiment =
                (FloatingActionButton) findViewById(R.id.btn_add_experiment);
        btnAddExperiment.setOnClickListener(view -> {
            topAppToolbar.setTitle("NEW EXPERIMENT");
        });


//        tabOptions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int id = tab.getPosition();
//                if(id == 0) {
//                    topAppToolbar.setTitle("HOME");
//                    viewPager.setCurrentItem(0);
//                } else if(id == 1) {
//                    topAppToolbar.setTitle("OWNED TAB");
//                    viewPager.setCurrentItem(1);
//                } else if(id == 2) {
//                    topAppToolbar.setTitle("SUBSCRIBED TAB");
//                    viewPager.setCurrentItem(2);
//                } else if(id == 3) {
//                    topAppToolbar.setTitle("ACTIVE TAB");
//                    viewPager.setCurrentItem(3);
//                } else if(id == 4) {
//                    topAppToolbar.setTitle("ENDED TAB");
//                    viewPager.setCurrentItem(4);
//                } else {
//                    // default - should be home
//                    viewPager.setCurrentItem(tab.getPosition());
//                    topAppToolbar.setTitle("HOME");
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

    }

    /* Necessary to create top bar icons */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }
}