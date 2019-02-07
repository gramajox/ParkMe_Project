package com.example.xgramajo.parkme_ids_2018.Ubication;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import bolts.Task;

import static java.lang.Thread.sleep;

public class UbicationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private MapView mapView;
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 15;
    boolean mLocationPermissionGranted;
    private Location currentLocation;
    private String direc;

    TextView tuVehiculo;
    TextView tuDirec;

    Button tuVehi;
    Button tuPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ubication, container, false);
        // Inflate the layout for this fragment

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)
                    getChildFragmentManager()
                            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tuVehiculo= view.findViewById(R.id.tuVehiculo);
        tuDirec = view.findViewById(R.id.tuDirec);

        tuVehi = view.findViewById(R.id.vehiculoBoton);
        tuPos = view.findViewById(R.id.tuPosicionBoton);

        tuVehi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonUbicarParking();
            }
        });

        tuPos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gpsact();
            }
        });

        tuVehiculo.setText("");
        tuDirec.setText("");

        obtenerPermisos();

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
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;

            } else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void ubicarCamara(LatLng latLng, float zoom){
        Log.d("Ubication","Ubicar cámara");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    //Checkear si ya estacionó y ubicar, si no sólo mostrar donde estás en azul
    //pero sin guardar datos en ParkingClass.
    private void checkearParking(){
        if(ParkingClass.getLat()!=0.0 && ParkingClass.getLng()!=0.0){
            botonUbicarParking();
            gpsact();
        } else {
            Toast.makeText(getContext(), "No iniciaste un Estacionamiento", Toast.LENGTH_LONG).show();
            gpsact();
        }
    }
    //Todavía no la usamos


    //Ubicar el vehículo a través de ParkingClass
    public void botonUbicarParking(){
        if (ParkingClass.getLat()!=0.0 && ParkingClass.getLng()!=0.0){

            LatLng vehiculo = new LatLng(ParkingClass.getLat(),ParkingClass.getLng());
            mMap.addMarker(new MarkerOptions().position(vehiculo)
                    .title("Tu Vehículo"));
            ubicarCamara(vehiculo, DEFAULT_ZOOM);
            tuVehiculo.setText(ParkingClass.getDireccion());

            Log.d("Ubication","LatLong Vehiculo: Lat="
                    +String.valueOf(ParkingClass.getLat())+" Long="
                    +String.valueOf(ParkingClass.getLng()));

            Toast.makeText(getContext(), "Vehículo Ubicado", Toast.LENGTH_SHORT).show();
        } else {
            tuVehiculo.setText("-");
            Toast.makeText(getContext(), "No iniciaste un Estacionamiento", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ubicarCamara(new LatLng(-37.0000000,-64.0000000),3); //Ubica el primer inicio de maps en Argentina.

        mMap.setOnMyLocationButtonClickListener(this); //Función cuando se presiona
        mMap.setOnMyLocationClickListener(this); //Aún no lo c.

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            }
            Log.d("Ubication","Mapa Ready");
            mMap.setMyLocationEnabled(true); //Este metodo muestra el boton GPS dentro del Mapa
            botonUbicarParking();
            return;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("Ubication", "Boton Localizar clickeado");
        gpsact();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {}//No se usa pero tiene que estar.


    /*
      -Chequeo del GPS ON/OFF
      -Solicitud de Activar el gps desde la app
      -AsyncTask para Obtener ubicación.
      Falta: Si el GPS no se activa, la AsyncTask termina después de 13 segundos.
                pero hay un método (onActivityResult()) para obtener la respuesta "cancelar" y ya determinar
                que el usuario no activó el gps y con eso intentar frenar la asynctask.
                        Estoy intentando entender ese método aún.
     */
    public void gpsact(){
        Context context = getContext();
        assert context != null;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.d("Ubication","GPS Activado");
            new posicionMaps().execute();
        } else {
            Log.d("Ubication","GPS Desactivado");
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            com.google.android.gms.tasks.Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(getContext()).checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {

                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<LocationSettingsResponse> task) {
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


    /*AsyncTask para obtener ubicación.
        info: duración máxima 13 segundos
     */
    private class posicionMaps extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute(){
            //load.setVisibility(View.VISIBLE);
            obtenerPermisos();
            Toast.makeText(getContext(),"Buscando Posición",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final boolean[] resultado = {false};
            final int[] cont = {0};

            while (!resultado[0] && cont[0] <13) {
                Log.d("Ubication", "Obteniendo Posición.");

                FusedLocationProviderClient mFusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(getContext());

                try {
                    if (mLocationPermissionGranted) {
                        com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {

                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                                if (task.isSuccessful()) {
                                    Log.d("Ubication", "posicionMaps Task Completada, analiza si dió null");
                                    if (task.getResult() != null) {
                                        resultado[0] = true;
                                        currentLocation = (Location) task.getResult();
                                        Log.d("Ubication", "Latitud: "+String.valueOf(currentLocation.getLatitude()));

                                        ubicarCamara(new LatLng(
                                                        currentLocation.getLatitude(),
                                                        currentLocation.getLongitude()),
                                                DEFAULT_ZOOM);
                                    } else {
                                        Log.d("Ubication", "posicionMaps: Aún no obtuvo ubicación");
                                        cont[0]++;
                                    }
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {
                    Log.d("Ubication", "Error Posición" + e);
                    //Toast.makeText(getContext(), "No pudo obtenerse su ubicación", Toast.LENGTH_SHORT);
                }
                try {
                    sleep(1000);
                    Log.d("Ubication", "posicionMaps Contador: "+String.valueOf(cont[0]));
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
            //load.setVisibility(View.INVISIBLE);
            if(!aBoolean){
                Log.d("Ubication", "NO se obtuvo ubicación");
                Toast.makeText(getContext(),"Ubicación no encontrada :(", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Ubication", "Ubicación encontrada!");
                //ParkingClass.setLatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                new direccionMaps().execute(); //Ejecuto una AsyncTask para obtener la Dirección.
            }
        }
    }

    //AsyncTask para obtener la Calle, Altura y mucho mas de una dirección.
    //Máximo 13 segundos de búsqueda.
    public class direccionMaps extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(),"Buscando Dirección",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            final boolean[] resultado = {false};
            final int[] cont = {0};
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
                    errorMessage = "Servicio no Disponible";
                    Log.e("Ubication", errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    errorMessage = "invalid lat long";
                    Log.e("Ubication", errorMessage + ". " +
                            "Latitude = " + currentLocation.getLatitude() +
                            ", Longitude = " +
                            currentLocation.getLongitude(), illegalArgumentException);
                }

                // Handle case where no address was found.
                if (addresses == null || addresses.size()  == 0) {
                    if (errorMessage.isEmpty()) {
                        errorMessage = "no adress found";
                        Log.e("Ubication", errorMessage);
                    }
                } else {

                    //Si encuentra una dirección entra por acá.
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<>();

                    // Fetch the address lines using getAddressLine.
                    for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }
                    Log.d("Ubication", "Adress Found");
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
                tuDirec.setText("-");
            } else {
                Log.d("Ubication","Dirección: "+direc);
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
                //Toast.makeText(getContext(), direc, Toast.LENGTH_SHORT).show();
                //ParkingClass.setDireccion(direc);
                Log.d("Ubication","Dirección paresada:"+direc);
                tuDirec.setText(direc);
                break;
            }
        }
    }


}
