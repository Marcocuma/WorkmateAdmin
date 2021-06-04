package com.example.workmateadmin;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    public Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_users, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        // Inicializa la conexion con el servidor y se conecta
        try {
            socket = IO.socket("https://workmateadmin.herokuapp.com");
        } catch (URISyntaxException e) {}
    }

    @Override
    // Se desconecta del servidor cuando se cierre la actividad
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        System.out.println("Cierra");
    }
    // Se desconecta del servidor cuando se cierra la aplicacion
    @Override
    protected void onStop() {
        super.onStop();
        socket.disconnect();
        System.out.println("Se desconecta");
    }

    //se vuelve a conectar cuando se vuelve a poner la aplicación en primer plano
    @Override
    protected void onResume() {
        super.onResume();
        socket.connect();
        System.out.println("Se conecta");
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }
}