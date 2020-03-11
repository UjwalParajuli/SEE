package com.ujwal.see;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnNavigationItemSelectedListener(nav_listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected_fragment = null;

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

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();
                    return true;

                }
            };
}
