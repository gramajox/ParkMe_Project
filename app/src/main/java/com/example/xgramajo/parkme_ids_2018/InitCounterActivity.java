package com.example.xgramajo.parkme_ids_2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;

public class InitCounterActivity extends AppCompatActivity {

    Button locationBtn, counterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_counter);

        locationBtn = (Button) findViewById(R.id.location_btn);
        counterBtn =  (Button) findViewById(R.id.btn_continue);

        counterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setActiveFragment("counterFragment");
                Intent myIntent = new Intent(InitCounterActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });
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
            case R.id.action_patent:
                HomeActivity.setActiveFragment("patentFragment");
                Intent myIntent = new Intent(this, HomeActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_settings:
                Toast.makeText(InitCounterActivity.this, "Configuración.", Toast.LENGTH_LONG).show();
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
}
