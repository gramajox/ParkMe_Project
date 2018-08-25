package com.example.xgramajo.parkme_ids_2018;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class InitCounterActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap; //hasta acá, es lo necesario para Maps.

    Button locationBtn, counterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_counter);

        locationBtn = findViewById(R.id.location_btn);
        counterBtn = findViewById(R.id.btn_continue);

        counterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setCounterFragment();
                Intent myIntent = new Intent(InitCounterActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        //Crear el mapa.
        MapFragment mapFragment1 = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment1.getMapAsync(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                HomeActivity.setHomeFragment();
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.action_patent:
                HomeActivity.setPatentFragment();
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            case R.id.log_out:
                logOut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        LoginActivity.logOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


    /**
     * Ubicación en el mapa.
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) { //Método que se ejecuta cuando el mapa está ready.
        mMap = googleMap;
        obtenerPermisos();

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return; //Permisos necesarios.
            }
            mMap.setMyLocationEnabled(true);
            obtenerUbicacion();

        }
    }


    private void obtenerUbicacion(){
        Log.d("deviceLocation","Obteniendo Posición.");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("deviceLocation Task","Completada, Ubicación encontrada");
                            Location currentLocation = (Location) task.getResult();

                            ubicarCamara(new LatLng(
                                    currentLocation.getLatitude(),
                                    currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.d("Error","Error Posición"+e);
            Toast.makeText(this,"No pudo obtenerse su ubicación",Toast.LENGTH_SHORT);
        }
    }


    private void ubicarCamara(LatLng latLng, float zoom){
        Log.d("tag:","Ubicar cámara");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


    /**
     * Preguntar al usuario por Permisos
     */
    private void obtenerPermisos() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                                            Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Manejo de Respuesta de los permisos.
     * Sirve para avisar al usuario qué pasa si no nos da los permisos.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //Code.
    }

    /**
     * Fin Ubicación en el mapa.
     * */
}
