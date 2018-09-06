package com.example.xgramajo.parkme_ids_2018.parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.R;
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
    private GoogleMap mMap;
    private Location currentLocation;//hasta acá, es lo necesario para Maps.

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
                finish();
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
                finish();
                return true;
            case R.id.action_patent:
                HomeActivity.setPatentFragment();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
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
        finish();
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
        ubicarCamara(new LatLng(-40.7333,-64.9333),3);//Latitud y Longitud de Las Grutas
                                                                    //Río Negro. Para Centrar el Mapa inicial.
                                                                    //en Argentina.
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

    @SuppressLint("ShowToast")
    private void obtenerUbicacion(){
        Log.d("deviceLocation","Obteniendo Posición.");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("deviceLocation Task","Completada, Ubicación encontrada");
                            if (task.getResult() != null) {
                                currentLocation = (Location) task.getResult();

                                ubicarCamara(new LatLng(
                                                currentLocation.getLatitude(),
                                                currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            } else {
                                Log.d("deviceLocation Task:","task null");
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.d("Error","Error Posición"+e);
            Toast.makeText(this,"No pudo obtenerse su ubicación",Toast.LENGTH_SHORT);
        }
    }

    //Este método ubica la vista del mapa mas cerca a las calles.
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

    /***
     * Chequeo del GPS ON/OFF, con AlertDialog
     */
    public void gpsact(View view){
        Context context = this.getApplicationContext();
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("Tag:","GPS Activado");
            obtenerUbicacion();
        } else {
            Log.d("Tag:","GPS Desactivado");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ups! GPS Desactivado :(");
            builder.setMessage("Para poder utilizar esta función es necesario que actives el GPS");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    /**Fin Chequeo GPS ON/OFF*/
    /**Chequeando Commit*/
}
