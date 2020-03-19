package com.ujwal.see;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
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



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

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
                                    } else {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_add).setChecked(true);
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

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();
                    return true;

                }
            };

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }





}
