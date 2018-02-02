package com.example.jaime.nevera;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class coordenadasActivity extends AppCompatActivity {

    // Elementos visuales
    private TextView textViewUbicacion;
    private Button buttonIniciar, buttonParar;
    private AlertDialog.Builder builder;

    // Ubicación
    LocationManager mlocManager = null;
    MyLocationListener mlocListener = null;

    //ESTO NO SE QUE ES
    private String hrefProyecto;


    // Debug
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);

        textViewUbicacion = (TextView) findViewById(R.id.text_view_ubicacion);

        buttonIniciar = (Button) findViewById(R.id.button_iniciar);
        buttonParar = (Button) findViewById(R.id.button_parar);



        // Empezar a buscar geolocalización cuando se pulsa este botón
        buttonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //LOCALIZACIÓN GPS
                mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mlocListener = new MyLocationListener();

                mlocListener.setMainActivity(coordenadasActivity.this);
                if (ActivityCompat.checkSelfPermission(coordenadasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(coordenadasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);
                Toast.makeText(coordenadasActivity.this, "Iniciando servicio...", Toast.LENGTH_SHORT).show();
                textViewUbicacion.setText("Localizando...");
            }
        });

        // Parar de buscar geolocalización cuando se pulsa este botón
        buttonParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(coordenadasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(coordenadasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if(mlocListener != null && mlocListener != null) {
                    mlocManager.removeUpdates(mlocListener);
                    textViewUbicacion.setText("Servicio parado.");
                    Toast.makeText(coordenadasActivity.this, "Servicio parado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class MyLocationListener implements LocationListener {

        coordenadasActivity coordenadasActivity;

        public coordenadasActivity getDatosActivity() {
            return coordenadasActivity;
        }

        public void setMainActivity(coordenadasActivity datosActivity) {
            this.coordenadasActivity = coordenadasActivity;
        }

        // Este método se ejecuta cuando el GPS está desactivado
        @Override
        public void onProviderDisabled(String provider) {
            textViewUbicacion.setText("GPS desactivado");
            Toast.makeText(coordenadasActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();

            // si está disponible la tarjeta SD y se puede escribir...

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este método se ejecuta cuando el GPS está activado
            textViewUbicacion.setText("GPS activado");

            textViewUbicacion.setText("Localizando...");
        }

        @Override
        public void onLocationChanged(Location location) {
            double latitud = location.getLatitude();
            double longitud = location.getLongitude();
            coordenadasActivity.this.location = location;

            String ubicacionActual = "Ubicación: " + latitud + ", " + longitud;


            textViewUbicacion.setText(ubicacionActual);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este método se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localización (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
