package com.example.xgramajo.parkme_ids_2018.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.EnableActivity;
import com.example.xgramajo.parkme_ids_2018.MonitorActivity;
import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PatentActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Activity homeAct;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private static String activatedFragment = "homeFragment";

    FragmentManager     fragmentManager     = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    HomeFragment        homeFragment        = new HomeFragment();
    ChronometerFragment counterFragment     = new ChronometerFragment();
    TimeLeftFragment    timeLeftFragment    = new TimeLeftFragment();

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

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
                } else {
                    if (Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail()).equals("xaviergramajo@gmail.com")) {
                        Log.d("HOMEACTI", firebaseAuth.getCurrentUser().getEmail());
                        ParkingClass.setAlreadyPaid(true);
                    }
                }
            }
        };
        setContentView(R.layout.activity_home);

        homeAct = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
/*
        if (ParkingClass.isAlreadyPaid()) {
            navigationView.getMenu().findItem(R.id.admin_option).setVisible(false);
        }
*/
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

        obtenerPermisos();

        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                moveTaskToBack(true);
            } else {
                Toast.makeText(this, "Presione nuevamente para cerrar",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_patent) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, PatentActivity.class));
            return true;

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_exit) {
            if (activatedFragment.equals("homeFragment")) {
                logOut();
            } else {
                Toast.makeText(this, "Tiene un estacionamiento activo",
                        Toast.LENGTH_SHORT).show();
            }

            return true;

        } else if (id == R.id.nav_monitor) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, MonitorActivity.class));
            return true;

        } else if (id == R.id.nav_enable) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, EnableActivity.class));
            return true;

        }

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

    /*
      Preguntar al usuario por Permisos de Ubicación.
     */
    public void obtenerPermisos() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
