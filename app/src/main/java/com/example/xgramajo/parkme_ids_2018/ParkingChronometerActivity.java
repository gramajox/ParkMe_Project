package com.example.xgramajo.parkme_ids_2018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PaymentChronometerActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Thread.sleep;

public class ParkingChronometerActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Activity parkAct;

    String numeroPatente;
    Button contBtn;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean mLocationPermissionGranted;
    private Location currentLocation;
    private String direc;

    ProgressBar load;
    TextView dir;

    Spinner spinnerPatente;
    FirebaseUser currentUser;
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_chronometer);

        mRootReference = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        parkAct = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setSubtitle("Pago Diferido");

        ParkingClass.setupFalse();

        spinnerPatente = findViewById(R.id.spinner_patente);

        //https://android--code.blogspot.com/2015/08/android-spinner-add-item-dynamically.html
        String[] userPatent = new String[] {};

        final List<String> userPatentList = new ArrayList<>(Arrays.asList(userPatent));

        final ArrayAdapter<String> spinnerArrayAdapterPatente = new ArrayAdapter<String>(this, R.layout.spinner_item, userPatentList);

        spinnerArrayAdapterPatente.setDropDownViewResource(R.layout.spinner_item);
        spinnerPatente.setAdapter(spinnerArrayAdapterPatente);

        contBtn = findViewById(R.id.btn_continue);

        dir = findViewById(R.id.dir);
        dir.setText("");

        load = findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);

        obtenerPermisos();

        new consultarSetup().execute();

        //Fuente: https://es.stackoverflow.com/questions/69656/evento-onclick-en-un-spinner

        spinnerPatente.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn2,
                                               View v2,
                                               int posicion2,
                                               long id2) {

                        numeroPatente = spn2.getSelectedItem().toString();

                    }
                    public void onNothingSelected(AdapterView<?> spn2) {
                    }
                });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParkingClass.setPatent(numeroPatente);
                ParkingClass.setDireccion(direc);
                ParkingClass.setupTrue();

                startActivity(new Intent(getApplicationContext(), PaymentChronometerActivity.class));
            }
        });

        mRootReference.child("Usuarios").child(currentUser.getUid()).child("Matriculas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String string = dataSnapshot.getValue(String.class);
                spinnerArrayAdapterPatente.add(string);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                spinnerArrayAdapterPatente.remove(string);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void obtenerPermisos() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getApplication()),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getApplication(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    //Método para activar la ubicación 8 segundos después de seleccionar
    // el modo de estacionamiento O
    // cuando el usuario toca el botón continuar.
    @SuppressLint("StaticFieldLeak")
    private class consultarSetup extends AsyncTask<Void, Void, Boolean> {
        int[] cont = {0};

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cont[0]=0;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            while (cont[0]<16){
                cont[0]++;
                if (ParkingClass.getSetup()){
                    break;
                } else {
                    try {
                        sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            gpsact(findViewById(android.R.id.content));
        }
    }

    /*AsyncTask para obtener ubicación.
        info: duración máxima 13 segundos
     */
    @SuppressLint("StaticFieldLeak")
    private class posicionMaps extends AsyncTask<Void, Void, Boolean>{

        protected void onPreExecute(){
            load.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Buscando Posición",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final boolean[] resultado = {false};
            final int[] cont = {0};

            while (!resultado[0] && cont[0] <13) {
                Log.d("deviceLocation", "Obteniendo Posición.");

                FusedLocationProviderClient mFusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(Objects.requireNonNull(getApplicationContext()));

                try {
                    if (mLocationPermissionGranted) {
                        Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Log.d("deviceLocation Task", "Completada, analiza si dió null");
                                    if (task.getResult() != null) {
                                        resultado[0] = true;
                                        currentLocation = (Location) task.getResult();
                                        Log.d("deviceLocation Task", String.valueOf(currentLocation.getLatitude()));
/***
                                        ubicarCamara(new LatLng(
                                                        currentLocation.getLatitude(),
                                                        currentLocation.getLongitude()),
                                                DEFAULT_ZOOM);*/
                                    } else {
                                        Log.d("deviceLocation Task: ", "Aún no obtuvo ubicación");
                                        cont[0]++;
                                    }
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {
                    Log.d("deviceLocation Task", "Error Posición" + e);
                    //Toast.makeText(getContext(), "No pudo obtenerse su ubicación", Toast.LENGTH_SHORT);
                }
                try {
                    sleep(1000);
                    Log.d("deviceLocation Task", String.valueOf(cont[0]));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!resultado[0]){
                return resultado[0];
            } else {
                return resultado[0];
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            load.setVisibility(View.INVISIBLE);
            if(!aBoolean){
                Log.d("deviceLocation Task: ", "NO se obtuvo ubicación");
                Toast.makeText(getApplicationContext(),"Ubicación no encontrada :(",Toast.LENGTH_LONG).show();
            } else {
                Log.d("deviceLocation Task: ", "Ubicación encontrada!");
                ParkingClass.setLatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                new direccionMaps().execute(); //Ejecuto una AsyncTask para obtener la Ubicación.
                //Toast.makeText(getContext(),"Yay!",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
      -Chequeo del GPS ON/OFF
      -Solicitud de Activar el gps desde la app
      -AsyncTask para Obtener ubicación.

      Falta: Si el GPS no se activa, la AsyncTask termina después de 13 segundos.
                pero hay un método (onActivityResult()) para obtener la respuesta "cancelar" y ya determinar
                que el usuario no activó el gps y con eso intentar frenar la asynctask.
                        Estoy intentando entender ese método aún.
     */
    public void gpsact(View view){
        Context context = getApplicationContext();
        assert context != null;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("Tag:","GPS Activado");
            new posicionMaps().execute();
        } else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            getParent(), /***ACA VA LA ACTIVITY-------------------------------------------------*/
                                            555);
                                    new posicionMaps().execute();
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });
        }
    }
    //Fin Chequeo GPS ON/OFF

    //AsyncTask para obtener la Calle, Altura y mucho mas de una dirección.
    //Máximo 13 segundos de búsqueda.
    private class direccionMaps extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Buscando Dirección",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final boolean[] resultado = {false};
            final int[] cont = {0};
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            String errorMessage = "";


            while (!resultado[0] && cont[0] <13) {

                try {
                    addresses = geocoder.getFromLocation(
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            // In this sample, get just a single address.
                            1);
                } catch (IOException ioException) {
                    // Catch network or other I/O problems.
                    errorMessage = getString(R.string.service_not_available);
                    Log.e(TAG, errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    errorMessage = getString(R.string.invalid_lat_long_used);
                    Log.e(TAG, errorMessage + ". " +
                            "Latitude = " + currentLocation.getLatitude() +
                            ", Longitude = " +
                            currentLocation.getLongitude(), illegalArgumentException);
                }

                // Handle case where no address was found.
                if (addresses == null || addresses.size()  == 0) {
                    if (errorMessage.isEmpty()) {
                        errorMessage = getString(R.string.no_address_found);
                        Log.e(TAG, errorMessage);
                    }
                } else {

                    //Si encuentra una dirección entra por acá.
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<>();

                    // Fetch the address lines using getAddressLine.
                    for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }
                    Log.i(TAG, getString(R.string.address_found));
                    direc = addressFragments.get(0);
                    resultado[0]=true;
                }
                cont[0]++;
                try{
                    sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            if (!resultado[0]){
                return resultado[0];
            } else {
                return resultado[0];
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(!aBoolean){
                dir.setText("no se encontró una Dirección");
            } else {
                Log.d("direccion",direc);
                parsearDireccion();
            }
        }
    }


    private void parsearDireccion(){
        String aux="";

        for (int i=0;i<direc.length();i++){
            if (direc.charAt(i) != 44){ //44 en ascii es la coma => ,
                aux = aux.concat(String.valueOf(direc.charAt(i)));
            } else {
                direc = aux;
                ParkingClass.setDireccion(direc);
                dir.setText(direc);
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
