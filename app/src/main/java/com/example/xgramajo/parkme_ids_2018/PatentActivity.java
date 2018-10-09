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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PatentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button registerPatent;
    EditText patentInput;
    String patentString;

    FirebaseUser currentUser;
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patent);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registerPatent = (Button) findViewById(R.id.patent_btn);
        patentInput = (EditText) findViewById(R.id.input_patente);

        registerPatent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patentString = patentInput.getText().toString();

                if (verifyPatent(patentString)) {

                    mRootReference.child("Usuarios").child(currentUser.getUid()).child("Matriculas").push().setValue(patentString);

                    HomeActivity.setHomeFragment();
                    Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Formato no valido, verifique lo ingresado.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /** Verifica el formato de una patente. Los formatos válidos son: ABC-123 y AB-123-CD
     *  Se considera que el input no contiene ni espacios ni guiones. Por ej: "ABC123" y "AB123CD" son válidas */
    private boolean verifyPatent(String input) {

        Pattern pattern;
        Matcher matcher;
        String PATENT_PATTERN;

        switch (input.length()) {
            case 6: // Estilo de patente ABC-123
                PATENT_PATTERN = "^[A-Z]{3}[0-9]{3}$";
                break;
            case 7: // Estilo de patente AA-123-BB
                PATENT_PATTERN = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$";
                break;
            default: // Longitud de patente incorrecta
                PATENT_PATTERN = "";
                break;
        }

        if (!PATENT_PATTERN.equals("")) { // La longitud de la patente coincide con una de las válidas y el patrón fue establecido
            pattern = Pattern.compile(PATENT_PATTERN);
            // Comparar la patente con el pattern
            matcher = pattern.matcher(input);
            return matcher.matches();
        }
        else {
            return false;
        }
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
                HomeActivity.setHomeFragment();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;

        } else if (id == R.id.nav_patent) {

            startActivity(new Intent(this, PatentActivity.class));
            finish();
            return true;

        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_exit) {

            logOut();
            return true;

        } else if (id == R.id.nav_monitor) {

            startActivity(new Intent(this, MonitorActivity.class));
            finish();
            return true;

        } else if (id == R.id.nav_enable) {

            startActivity(new Intent(this, EnableActivity.class));
            finish();
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

}
