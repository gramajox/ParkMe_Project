package com.example.xgramajo.parkme_ids_2018.Parking;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xgramajo.parkme_ids_2018.Adapters.SectionsPageAdapter;
import com.example.xgramajo.parkme_ids_2018.Home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.Parking.LocationFragment;
import com.example.xgramajo.parkme_ids_2018.Parking.SetupFragment;
import com.example.xgramajo.parkme_ids_2018.Parking.SummaryFragment;
import com.example.xgramajo.parkme_ids_2018.R;

public class ParkingActivity extends AppCompatActivity {

    public static boolean prePayment;
    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void setupViewPager(ViewPager viewPager) {

        mSectionsPageAdapter.addFragment(new SetupFragment(), "Iniciar");
        mSectionsPageAdapter.addFragment(new LocationFragment(), "Lugar");

        viewPager.setAdapter(mSectionsPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
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

    public static void setPrePaymentTrue() {
        prePayment = true;
    }

    public static void setPrePaymentFalse() {
        prePayment = false;
    }

}
