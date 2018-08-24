package com.example.xgramajo.parkme_ids_2018;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InitCounterActivity extends AppCompatActivity {


    Button locationBtn, counterBtn;

    private TextView lat;
    private TextView lon;
    private TextView dir;
    private String provider;

    public LocationManager handle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_counter);

        locationBtn = findViewById(R.id.location_btn);
        counterBtn =  findViewById(R.id.btn_continue);

        counterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setCounterFragment();
                Intent myIntent = new Intent(InitCounterActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        lat = findViewById(R.id.lat);

        lat.setText("test");

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









    /**Intento obtener las coordenadas por GPS*/

    @SuppressLint("MissingPermission")
    public void IniciarServicio() {
        handle = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);

        provider = handle.getBestProvider(c, true);

        handle.requestLocationUpdates(provider, 10000, 1, (LocationListener) this);
        muestraPosicionActual();
    }

    public void muestraPosicionActual() {

        @SuppressLint("MissingPermission") Location location = handle.getLastKnownLocation(provider);
        if (location!=null){
            lat.setText((int) location.getLatitude());
            lon.setText((int) location.getLongitude());
        } else {
            lat.setText("Desconocido");
            lon.setText("Desconocido");
        }
        setDirection(location);
    }

    public void setDirection(Location loc){
        if (loc != null){
            if(loc.getLatitude()!=0.0 && loc.getLongitude()!=0.0){
                try{
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
                    if(!list.isEmpty()){
                        Address direccion = list.get(0);
                        dir.setText(direccion.getAddressLine(0));
                    }
                } catch (IOException e){dir.setText(""+e);}
            }
        }
    }

    public void detenerServicio(){
        handle.removeUpdates((LocationListener) this);
        lat.setText(null);
        lon.setText(null);
        dir.setText(null);
        Toast.makeText(this,"Ubicación desactivada",Toast.LENGTH_SHORT).show();
    }

    /**Fin de mis intentos :)*/
}
