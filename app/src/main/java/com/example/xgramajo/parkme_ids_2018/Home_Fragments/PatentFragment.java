package com.example.xgramajo.parkme_ids_2018.Home_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatentFragment extends Fragment {

    Button registerPatent;
    EditText patentInput;
    String patentString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patent, container, false);

        registerPatent = (Button) view.findViewById(R.id.patent_btn);
        patentInput = (EditText) view.findViewById(R.id.input_patente);

        registerPatent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patentString = patentInput.getText().toString();

                if (verifyPatent(patentString)) {
                    HomeActivity.setActiveFragment("homeFragment");
                    Intent myIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getContext(), "Formato no valido, verifique lo ingresado.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    /**ACA HAY QUE CORREGIR EL PATRON PARA LA VERIFICACION DEL FORMATO DE MATRICULA*/
    private boolean verifyPatent(String input) {
        /**
        if (input.length() == 6 || input.length() == 8) {
            Pattern pattern;
            Matcher matcher;

        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
        (?=\\S+$)          # no whitespace allowed in the entire string
        .{4,}             # anything, at least four places though
        $                 # end-of-string

            final String PATENT_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$)$";
            pattern = Pattern.compile(PATENT_PATTERN);

            matcher = pattern.matcher(input);

            return matcher.matches();

        } else return false;*/
        return true;
    }

}
