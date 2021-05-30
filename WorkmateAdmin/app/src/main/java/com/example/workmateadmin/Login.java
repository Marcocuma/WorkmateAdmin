package com.example.workmateadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private ImageButton loginB;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginB = findViewById(R.id.button_login);
        password = findViewById(R.id.editTextTextPassword);
        // Comprueba que la contraseña sea la misma que la guardada y si es asi entra en la aplicacion
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con1 = password.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                // Admin es la contraseña por defecto, pudiendo cambiarse cambiando el string
                if(con1.equals(prefs.getString("password","admin"))){
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Login.this, R.string.incorrectpass, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}