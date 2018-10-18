package com.example.xgramajo.parkme_ids_2018.parking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.adapters.SectionsPageAdapter;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Objects;

public class ParkingActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    ViewPager mViewPager;

    @SuppressLint("StaticFieldLeak")
    public static Activity parkAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        parkAct = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (ParkingClass.isPrepayment()) {
            toolbar.setSubtitle("Pago Adelantado");
        } else {
            toolbar.setSubtitle("Pago Diferido");
        }

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
/*
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
*/
    }

    private void setupViewPager(ViewPager viewPager) {

        mSectionsPageAdapter.addFragment(new SetupFragment(), "Iniciar");
        mSectionsPageAdapter.addFragment(new LocationFragment(), "Lugar");

        viewPager.setAdapter(mSectionsPageAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}
