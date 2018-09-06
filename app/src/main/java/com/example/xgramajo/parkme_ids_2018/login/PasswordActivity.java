package com.example.xgramajo.parkme_ids_2018.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword, backbtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        passwordEmail = (EditText)findViewById(R.id.pwd_email);
        resetPassword = (Button)findViewById(R.id.pwd_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        backbtn = (Button) findViewById(R.id.back_btn);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = passwordEmail.getText().toString().trim();

                if(useremail.equals("")){
                    Toast.makeText(PasswordActivity.this, "Por favor, ingrese su correo electronico.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordActivity.this, "Se envió un mail para restablecer la contraseña.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                                finish();
                            }else{
                                Toast.makeText(PasswordActivity.this, "Error al enviar el mail.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

}