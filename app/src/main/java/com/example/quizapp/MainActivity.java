package com.example.quizapp;


import android.os.Bundle;
import android.view.MenuItem;


import android.widget.FrameLayout;
import android.widget.TextView;


import com.example.quizapp.DataBase.dbQuery;
import com.example.quizapp.Fragment.AccountFragment;
import com.example.quizapp.Fragment.CategoryFragment;
import com.example.quizapp.Fragment.LeaderBoardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
 import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;
    private TextView drawerProfileName, drawerProfileText;
    private NavigationBarView.OnItemSelectedListener onItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            setFragment(new CategoryFragment());
                            return true;
                        case R.id.nav_leaderboard:
                            setFragment(new LeaderBoardFragment());
                            return true;
                        case R.id.nav_account:
                            setFragment(new AccountFragment());
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);

        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_textimg);

        String name = dbQuery.myProfile.getName();
        drawerProfileName.setText(name);
        drawerProfileText.setText(name.toUpperCase().substring(0,1));

        setFragment(new CategoryFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setFragment(Fragment Fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), Fragment);
        transaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case R.id.nav_home:
                setFragment(new CategoryFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_leaderboard:
                setFragment(new LeaderBoardFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_account:
                setFragment(new AccountFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_account);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }

}