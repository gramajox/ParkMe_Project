package com.example.xgramajo.parkme_ids_2018.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.Home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Objects;

public class SummaryFragment extends Fragment {

    Button payBtn, backBtn;
    ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_summary, container, false);

        TextView matricula = (TextView) view.findViewById(R.id.txt_res_msj02);
        TextView time = (TextView) view.findViewById(R.id.txt_res_msj03);
        TextView mount = (TextView) view.findViewById(R.id.textView4);

        payBtn = (Button) view.findViewById(R.id.pay_btn);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        viewPager = (ViewPager) Objects.requireNonNull(getActivity()).findViewById(R.id.container);

        matricula.setText(SetupFragment.getMatricula());
        time.setText(SetupFragment.getSelectedTime());
        mount.setText(SetupFragment.getMount());

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setTimeLeftFragment();
                startActivity(new Intent(getContext(), HomeActivity.class));
                //startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }

}
