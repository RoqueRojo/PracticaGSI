package com.example.lab_gsi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.lab_gsi.R;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab_gsi.Dominio.FetchURL;
import com.example.lab_gsi.Dominio.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.example.lab_gsi.Dominio.Recomendacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;

public class mapa extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private ArrayList<Recomendacion> recomendaciones;

    private GoogleMap map;
    private String tipo;
    private Button btnGPSllegar;
    private Polyline cuPolyline;
    private Recomendacion recomendacion;
    private String latitud;
    private String longitud;
    private MarkerOptions place1, place2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int REQUEST_LOCATION = 1;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Mapa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo = (String) getIntent().getSerializableExtra("Tipo");

        recomendaciones = (ArrayList<Recomendacion>) getIntent().getSerializableExtra("Recomendaciones");

        btnGPSllegar = findViewById(R.id.btnGPSllegar);
        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.idMapa);

        mapFragment.getMapAsync(this);
        recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recomendacion");

        place1 = new MarkerOptions().position(new LatLng(Double.parseDouble(recomendacion.getLatitud()), Double.parseDouble(recomendacion.getLongitud()))).title(recomendacion.getNombre());

        btnGPSllegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPS();

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.addMarker(place1);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(recomendacion.getLatitud()), Double.parseDouble(recomendacion.getLongitud())))
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onTaskDone(Object... values) {
        if (cuPolyline != null)
            cuPolyline.remove();
        cuPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(mapa.this, lista.class);
            intent.putExtra("Tipo",tipo);
            intent.putExtra("Recomendaciones", recomendaciones);
            startActivity(intent);
            finish();
        } if (item.getItemId() == R.id.btnAutores) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Aplicación creada por Antonio Felipe Rojo y Roque Rojo Bacete");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void GPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new android.app.AlertDialog.Builder(mapa.this)
                    .setTitle("Permiso necesario").setMessage("Encienda su GPS")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(mapa.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (mapa.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                Location location3 = locationManager.getLastKnownLocation(LOCATION_SERVICE);


                //if (location != null) {
                  //  latitud = String.valueOf(location.getLatitude());
                   // longitud = String.valueOf(location.getLongitude());
                   // showToast("Longitud: " + longitud + ", latitud: " + latitud);
                //} else
                if (location1 != null) {
                    latitud = String.valueOf(location1.getLatitude());
                    longitud = String.valueOf(location1.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else if (location2 != null) {
                   latitud = String.valueOf(location2.getLatitude());
                    longitud = String.valueOf(location2.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                }else if(location3!=null){
                    latitud = String.valueOf(location3.getLatitude());
                    longitud = String.valueOf(location3.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                }
                if(longitud==null&&latitud==null){
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitud = String.valueOf(location.getLatitude());
                            longitud = String.valueOf(location.getLongitude());
                            //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    locationManager.requestLocationUpdates("gps",5000,0,locationListener);
                }
                if(latitud !=null && longitud!= null) {
                    place2 = new MarkerOptions().position(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud))).title("Tú");
                    map.addMarker(place2);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)))
                            .zoom(14)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    new FetchURL(mapa.this).execute(getUrl("driving"), "driving");
                }else{
                    //showToast("No se puede obtener su localización, intentelo mas tarde");
                }
            }
        }
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(mapa.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    private String getUrl(  String directionMode) {
        String str_origin = "origin=" + latitud + "," + longitud;
        String str_dest = "destination=" + recomendacion.getLatitud() + "," + recomendacion.getLongitud();
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        //String url ="https://maps.googleapis.com/maps/api/directions/json?origin="+recomendacion.getLatitud()+","+recomendacion.getLongitud()+"&destination="+latitud+","+longitud;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyBYn9FtVcCcwQZm3NdhwxuOb1pIBolFytI";
        return url;
    }
}
