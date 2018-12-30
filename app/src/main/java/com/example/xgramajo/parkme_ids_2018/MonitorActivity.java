package com.example.xgramajo.parkme_ids_2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;

import java.util.ArrayList;

public class MonitorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ListView listPatents, listDirections, listTimes;
    ArrayList<String> patentesArray = new ArrayList<>();
    ArrayList<String> directionsArray = new ArrayList<>();
    ArrayList<String> timesArray = new ArrayList<>();

    ArrayAdapter<String> adapterPatente, adapterDirection, adapterTime;

    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (!HomeActivity.getIsAdmin()) {
            navigationView.getMenu().findItem(R.id.admin_option).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(this);

        listPatents = findViewById(R.id.list_patent);
        listDirections = findViewById(R.id.list_direction);
        listTimes = findViewById(R.id.list_time);


        adapterPatente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patentesArray);
        adapterDirection = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directionsArray);
        adapterTime = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timesArray);

        listPatents.setAdapter(adapterPatente);
        listDirections.setAdapter(adapterDirection);
        listTimes.setAdapter(adapterTime);

        mRootReference.child("Habilitados").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String patenteString = dataSnapshot.child("Matrícula").getValue(String.class);
                adapterPatente.add(patenteString);

                String directionString = dataSnapshot.child("Localización").getValue(String.class);
                adapterDirection.add(directionString);

                String timeString = dataSnapshot.child("HoraFin").getValue(String.class);
                adapterTime.add(timeString);

                Log.d("MONITOR DE MATRICULASS", dataSnapshot.child("Matrícula").getValue(String.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                adapterPatente.remove(dataSnapshot.child("Matrícula").getValue(String.class));
                adapterDirection.remove(dataSnapshot.child("Localización").getValue(String.class));
                adapterTime.remove(dataSnapshot.child("HoraFin").getValue(String.class));

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
            onBackPressed();
            return true;

        } else if (id == R.id.nav_patent) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, PatentActivity.class));
            finish();
            return true;

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_exit) {
            drawer.closeDrawer(GravityCompat.START);
            logOut();
            return true;

        } else if (id == R.id.nav_monitor) {
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_enable) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, EnableActivity.class));
            finish();
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
}
