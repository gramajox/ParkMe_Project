package com.example.xgramajo.parkme_ids_2018.parking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PaymentActivity;
import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class LocationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private Location currentLocation;//Hasta acá necesario para Maps.


    ProgressBar load;
    Button startBtn, payBtn, backBtn;
    ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_location, container, false);

        payBtn = view.findViewById(R.id.pay_btn);
        //backBtn = view.findViewById(R.id.back_btn);
        startBtn = view.findViewById(R.id.btn_start);

        load = view.findViewById(R.id.load);
        load.setVisibility(View.INVISIBLE);

        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.container);

        obtenerPermisos();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);   //Crear el Mapa

        setInfo(ParkingClass.isPrepayment());

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*cargar a ParkClass la ubicación*/
                //ParkingClass.setLocation(Double.toString(currentLocation.getLatitude()) + Double.toString(currentLocation.getLongitude()));
                HomeActivity.homeAct.finish();
                HomeActivity.setCounterFragment();
                startActivity(new Intent(getContext(), HomeActivity.class));
                getActivity().finish();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*cargar a ParkClass la ubicación*/
                //ParkingClass.setLocation(Double.toString(currentLocation.getLatitude()) + Double.toString(currentLocation.getLongitude()));

                /*Seteo PRE_PAYMENT true para que al terminar mercadopago vaya al timeLeft y no al home*/
                PaymentActivity.setPRE_PAYMENT(true);
                startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });

        /*
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        */

        return view;
    }

    /*
      Preguntar al usuario por Permisos
     */
    public void obtenerPermisos() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void ubicarCamara(LatLng latLng, float zoom){
        Log.d("tag:","Ubicar cámara");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    //Método que se ejecuta al estar el mapa Ready.
    @Override
    public void onMapReady(GoogleMap mapa) {
        mMap = mapa;

        ubicarCamara(new LatLng(-37.0000000,-64.0000000),3); //Ubica el primer inicio de maps en Argentina.

        mMap.setOnMyLocationButtonClickListener(this); //Función cuando se presiona
        mMap.setOnMyLocationClickListener(this); //Aún no lo c.

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            }
            mMap.setMyLocationEnabled(true); //Este metodo muestra el boton GPS dentro del Mapa
            return;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {} //Por ahora no la usamos

    //Función del boton de localizar que está sobre el mapa
    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("Boton Localizar GPS: ", "clickeado");
        gpsact(getView());
        return false;
    }

    /*AsyncTask para obtener ubicación.
        info: duración máxima 13 segundos
     */
    private class posicionMaps extends AsyncTask<Void, Void, Boolean>{

        protected void onPreExecute(){
            load.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(),"Buscando Posición",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final boolean[] resultado = {false};
            final int[] cont = {0};

            while (!resultado[0] && cont[0] <13) {
                Log.d("deviceLocation", "Obteniendo Posición.");

                FusedLocationProviderClient mFusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

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

                                        ubicarCamara(new LatLng(
                                                        currentLocation.getLatitude(),
                                                        currentLocation.getLongitude()),
                                                DEFAULT_ZOOM);
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
                Toast.makeText(getContext(),"Ubicación no encontrada :(",Toast.LENGTH_LONG).show();
            } else {
                Log.d("deviceLocation Task: ", "Ubicación encontrada!");
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
        Context context = this.getContext();
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
                    LocationServices.getSettingsClient(this.getActivity()).checkLocationSettings(builder.build());

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
                                            getActivity(),
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


    private void setInfo(boolean prePayment) {
        if (prePayment) {

            startBtn.setVisibility(View.GONE);

        } else {

            payBtn.setVisibility(View.GONE);

        }
    }
}