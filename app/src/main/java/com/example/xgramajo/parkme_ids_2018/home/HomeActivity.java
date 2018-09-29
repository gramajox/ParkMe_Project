package com.example.xgramajo.parkme_ids_2018.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.xgramajo.parkme_ids_2018.EnableActivity;
import com.example.xgramajo.parkme_ids_2018.MonitorActivity;
import com.example.xgramajo.parkme_ids_2018.PatentActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private static String activatedFragment = "homeFragment";

    FragmentManager     fragmentManager     = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    HomeFragment        homeFragment        = new HomeFragment();
    CounterFragment     counterFragment     = new CounterFragment();
    TimeLeftFragment    timeLeftFragment    = new TimeLeftFragment();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    sendToLogin();
                }
            }
        };
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switch (activatedFragment) {
            case "homeFragment":
                fragmentTransaction.add(R.id.fragment_container, homeFragment);
                break;
            case "counterFragment":
                fragmentTransaction.add(R.id.fragment_container, counterFragment);
                break;
            case "timeLeftFragment":
                fragmentTransaction.add(R.id.fragment_container, timeLeftFragment);
                break;
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            
            startActivity(new Intent(this, HomeActivity.class));
            return true;

        } else if (id == R.id.nav_patent) {

            startActivity(new Intent(this, PatentActivity.class));
            return true;

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_exit) {

            logOut();
            return true;

        } else if (id == R.id.nav_monitor) {

            startActivity(new Intent(this, MonitorActivity.class));
            return true;

        } else if (id == R.id.nav_enable) {

            startActivity(new Intent(this, EnableActivity.class));
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        LoginActivity.logOut();
        sendToLogin();
        finish();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

    public static void setHomeFragment() {
        activatedFragment = "homeFragment";
    }
    public static void setCounterFragment() {
        activatedFragment = "counterFragment";
    }
    public static void setTimeLeftFragment() {
        activatedFragment = "timeLeftFragment";
    }
}
