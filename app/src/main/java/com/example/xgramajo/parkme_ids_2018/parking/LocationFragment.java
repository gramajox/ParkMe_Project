package com.example.xgramajo.parkme_ids_2018.parking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    boolean mLocationPermissionGranted;
    private GoogleMap mMap;//Hasta acá necesario para Maps.

    Button startBtn, payBtn, backBtn;
    ViewPager viewPager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_location, container, false);

        payBtn = (Button) view.findViewById(R.id.pay_btn);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        startBtn = (Button) view.findViewById(R.id.btn_start);

        viewPager = (ViewPager) Objects.requireNonNull(getActivity()).findViewById(R.id.container);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Crear el Mapa

        setInfo(ParkingActivity.prePayment);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setCounterFragment();
                Intent myIntent = new Intent(getContext(), HomeActivity.class);
                startActivity(myIntent);
                getActivity().finish();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {//Método que se ejecuta al estar el mapa Ready.
        mMap = googleMap;

        obtenerPermisos();

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            obtenerUbicacion();
        }
    }

    @SuppressLint("ShowToast")
    private void obtenerUbicacion(){
        Log.d("deviceLocation","Obteniendo Posición.");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getContext()));

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("deviceLocation Task","Completada, Ubicación encontrada");
                            if (task.getResult() != null) {
                                Location currentLocation = (Location) task.getResult();

                                ubicarCamara(new LatLng(
                                                currentLocation.getLatitude(),
                                                currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.d("Error","Error Posición"+e);
            Toast.makeText(this.getContext(),"No pudo obtenerse su ubicación",Toast.LENGTH_SHORT);
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

    private void setInfo(boolean prePayment) {
        if (prePayment) {

            startBtn.setVisibility(View.GONE);

        } else {

            payBtn.setVisibility(View.GONE);

        }
    }

}