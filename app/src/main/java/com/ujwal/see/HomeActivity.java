package com.ujwal.see;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnNavigationItemSelectedListener(nav_listener);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                    Fragment selected_fragment = null;

                    getSupportFragmentManager().addOnBackStackChangedListener(
                            new FragmentManager.OnBackStackChangedListener() {
                                public void onBackStackChanged() {
                                    Fragment current = getCurrentFragment();
                                    if (current instanceof HomeFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
                                    }
                                    else if(current instanceof SearchFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_search).setChecked(true);
                                    }
                                    else if (current instanceof AddFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_add).setChecked(true);
                                    }
                                    else if (current instanceof NotificationFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_notification).setChecked(true);
                                    }
                                    else if (current instanceof ProfileFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_user).setChecked(true);
                                    }
                                }
                            });

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selected_fragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selected_fragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            selected_fragment = new AddFragment();
                            break;
                        case R.id.nav_notification:
                            selected_fragment = new NotificationFragment();
                            break;
                        case R.id.nav_user:
                            selected_fragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack(null).commit();
                    return true;

                }
            };

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 1) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // MainActivity.super.onBackPressed();
                            finish();
                            moveTaskToBack(true);
                        }
                    }).create().show();
        }


    }


}
