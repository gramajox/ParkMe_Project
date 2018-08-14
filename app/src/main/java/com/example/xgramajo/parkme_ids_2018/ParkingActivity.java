package com.example.xgramajo.parkme_ids_2018;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.Adapters.SectionsPageAdapter;
import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.Parking_Fragments.LocationFragment;
import com.example.xgramajo.parkme_ids_2018.Parking_Fragments.SetupFragment;
import com.example.xgramajo.parkme_ids_2018.Parking_Fragments.SummaryFragment;

public class ParkingActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);/*
        mViewPager.setAdapter(mSectionsPagerAdapter);*/
        setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void setupViewPager(ViewPager viewPager) {

        mSectionsPageAdapter.addFragment(new SetupFragment(), "Iniciar");
        mSectionsPageAdapter.addFragment(new LocationFragment(), "Lugar");
        mSectionsPageAdapter.addFragment(new SummaryFragment(), "Resumen");

        viewPager.setAdapter(mSectionsPageAdapter);
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
                //HomeActivity.setActiveFragment("homeFragment");
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

}
