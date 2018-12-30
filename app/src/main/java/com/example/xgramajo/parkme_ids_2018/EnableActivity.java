package com.example.xgramajo.parkme_ids_2018;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @SuppressLint("StaticFieldLeak")
    private static TextView montoCalculado;
    String[] arrayMontos;

    String tiempoSeleccionado;
    int montoAsociado;
    Boolean timeIsSelected;
    Button contBtn;

    ProgressBar load;
    EditText dirInput;

    Button registerPatent;
    EditText patentInput;
    String patentString, directionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable);

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

        patentInput = findViewById(R.id.input_patente);
        Spinner spinnerDur = findViewById(R.id.spinner_duracion);

        montoCalculado = findViewById(R.id.txt_monto);
        contBtn = findViewById(R.id.btn_continue);

        dirInput = findViewById(R.id.dir);

        spinnerDur.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    public void onItemSelected(AdapterView<?> spn,
                                               View v,
                                               int posicion,
                                               long id) {

                        ((TextView) v).setTextSize(15);

                        tiempoSeleccionado = spn.getSelectedItem().toString();
                        posicion = spn.getSelectedItemPosition();

                        if (posicion == 0){

                            ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));

                            registerPatent.setBackgroundColor(Color.GRAY);

                            timeIsSelected = false;
                        }
                        else {
                            ((TextView) v).setTextColor(Color.BLACK);
                            registerPatent.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                            timeIsSelected = true;
                        }

                        arrayMontos = getResources().getStringArray(R.array.array_montos);
                        montoCalculado.setText("$ " + arrayMontos[posicion]);

                        /*Porque el primer elemento es un string $$$ y no puedo pasarlo a INT*/
                        if (posicion != 0) {
                            montoAsociado = Integer.parseInt(arrayMontos[posicion]);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        registerPatent = (Button) findViewById(R.id.patent_btn);
        patentInput = (EditText) findViewById(R.id.input_patente);

        registerPatent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                patentString = patentInput.getText().toString();
                directionString = dirInput.getText().toString();

                if (verifyPatent(patentString)) {
                    if (!timeIsSelected){
                        Toast.makeText(getApplicationContext() ,"Seleccione un tiempo", Toast.LENGTH_LONG).show();
                    }
                    else {

                        if (!directionString.equals("")) {

                            ParkingClass.setTime(tiempoSeleccionado);
                            ParkingClass.setPrepayment(true);
                            FirebaseController.writeAvaliablePatent(patentString, directionString, ParkingClass.getEndTime());

                            startActivity(new Intent(getApplicationContext(), MonitorActivity.class));
                            finish();

                            Toast.makeText(getApplicationContext(), "Listo! Matrícula registrada.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Ingrese una ubicación.", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Formato de matrícula no valido.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public static boolean verifyPatent(String input) {

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
            startActivity(new Intent(this, MonitorActivity.class));
            finish();
            return true;

        } else if (id == R.id.nav_enable) {
            drawer.closeDrawer(GravityCompat.START);
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
