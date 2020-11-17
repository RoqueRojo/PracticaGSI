package com.example.lab_gsi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lab_gsi.Dominio.Recomendacion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;


public class CrearActivity extends AppCompatActivity {

    private int PICK_IMAGE = 0;
    private int PICK_PHOTO = 1;
    private int REQUEST_LOCATION = 1;
    private Uri imageUri;
    private Bitmap bitmap;
    private String image;
    private ImageView imageView;
    private ImageView foto;
    private String pathToFile;
    private long captureTime;
    private String tipo;
    private String modificar;
    private Recomendacion recomendacion;

    private String longitud;
    private String latitud;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Button btnSeleccionar;
    private Button btnCrear;
    private TextView txtNombre;
    private TextView txtDesc;
    private Spinner spOpciones;

    private ArrayList<Recomendacion> recomendaciones;
    private Recomendacion nuevaRecomendacion;

    private static final int GALLERY_REQUEST_CODE = 123;

    Spinner opciones;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        nuevaRecomendacion = new Recomendacion();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Información");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recomendaciones = (ArrayList<Recomendacion>) getIntent().getSerializableExtra("Recomendaciones");
System.out.println("Numero recomendaciones: " + recomendaciones.size());

        tipo = (String) getIntent().getSerializableExtra("Tipo");
        modificar = (String) getIntent().getSerializableExtra("Modificar");
        opciones = (Spinner)findViewById(R.id.spOpciones);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter);

            recomendacion = (Recomendacion) getIntent().getSerializableExtra("Recomendacion");


        btnSeleccionar= findViewById(R.id.btnSeleccionar);
        imageView = findViewById(R.id.imgFoto);
        btnCrear = findViewById(R.id.btnCrear);
        txtNombre = findViewById(R.id.txtNombre);
        txtDesc = findViewById(R.id.txtDescripciona);
        spOpciones = findViewById(R.id.spOpciones);

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), GALLERY_REQUEST_CODE);
                //////seleccionarFoto();

                if (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

                }
                else {

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    File imageViewPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String path = imageViewPath.getPath();
                    intent.setDataAndType(Uri.parse(path), "image/*");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), GALLERY_REQUEST_CODE);
                    }
                }
                //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //startActivityForResult(intent, PICK_IMAGE);
            }
        });

        /*btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                nuevaRecomendacion.setNombre(txtNombre.getText().toString());
                nuevaRecomendacion.setComentario(txtDesc.getText().toString());
                if(spOpciones.getSelectedItemPosition() == 0){
                    nuevaRecomendacion.setTipo("restaurante");
                }
                else{
                    nuevaRecomendacion.setTipo("museo");
                }

            }

        });*/
        if(modificar.equals("modificar")){
            btnCrear.setText("Modificar");
            txtNombre.setText(recomendacion.getNombre());
            txtDesc.setText(recomendacion.getComentario());
            if(recomendacion.getTipo().equals("restaurante")){
                spOpciones.setSelection(0);
            }
            else{
                spOpciones.setSelection(1);
            }

            imageView.setImageURI(Uri.parse(recomendacion.getImagenBit()));
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode== GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
        Uri imageData = data.getData();

        imageView.setImageURI(imageData);



        nuevaRecomendacion.setImagenBit(imageData.toString());
        System.out.println("Foto: "+ nuevaRecomendacion.getImagenBit());
    }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.btnAutores:

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void seleccionarFoto() {
        if (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

   /* public void seleccionarFoto(View view) {

        if (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                ( CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        }
    }*/

   /*public void seleccionarFoto(View view) {
        if (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CrearActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_LOCATION);

        } else if
        (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (CrearActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

        } else {
            File path = Environment.getExternalStorageDirectory();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureTime = System.currentTimeMillis();
            pathToFile = path.getAbsolutePath()
                    + "/DCIM/Camera/" + captureTime + ".jpg";
            File foto = new File(pathToFile);
            Uri output = Uri.fromFile(foto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivityForResult(Intent.createChooser(intent, "Hacer Foto"), PICK_PHOTO);
        }

    }*/

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            if(modificar.equals("modificar")) {
                Intent intent = new Intent(CrearActivity.this, lista.class);
                intent.putExtra("Tipo", "modificar");
                System.out.println("Salgo de modificar");
                System.out.println("En modificar: " + recomendaciones.size());
                intent.putExtra("Recomendaciones", recomendaciones);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(CrearActivity.this, MenuActivity.class);
                intent.putExtra("Tipo", tipo);
                intent.putExtra("Recomendaciones", recomendaciones);
                startActivity(intent);
                finish();
            }
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
            new android.app.AlertDialog.Builder(CrearActivity.this)
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
            if (ActivityCompat.checkSelfPermission(CrearActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (CrearActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(CrearActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                Location location3 = locationManager.getLastKnownLocation(LOCATION_SERVICE);


                //if (location != null) {
                  //  latitud = String.valueOf(location.getLatitude());
                   // longitud = String.valueOf(location.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                //} else
                if (location1 != null) {
                    latitud = String.valueOf(location1.getLatitude());
                    longitud = String.valueOf(location1.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else if (location2 != null) {
                    latitud = String.valueOf(location2.getLatitude());
                    longitud = String.valueOf(location2.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                } else
                if (location3 != null) {
                    latitud = String.valueOf(location3.getLatitude());
                    longitud = String.valueOf(location3.getLongitude());
                    //showToast("Longitud: " + longitud + ", latitud: " + latitud);
                }
                if (longitud == null && latitud == null) {
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
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                }
            }
        }
    }

    public void showToast(String r) {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(CENTER, 0, 0);
        TextView view = new TextView(CrearActivity.this);
        view.setBackgroundColor(Color.DKGRAY);
        view.setTextColor(Color.WHITE);
        view.setText("\"" + r + "\"");
        view.setPadding(10, 10, 10, 10);
        toast.setView(view);
        toast.show();
    }

    public void crear(View view) {
        if (txtNombre.getText().length() == 0 || txtDesc.getText().length() == 0) {
            showToast("Rellene los campos");
        }
        else{
            if (modificar.equals("modificar")) {
                int position = 0;
                for (int i = 0; i < recomendaciones.size(); i++) {
                    if (recomendaciones.get(i).getNombre().equals(recomendacion.getNombre())) {
                        position = i;
                    }
                }
                GPS();
                System.out.println("Entro en modificar");
                recomendaciones.get(position).setNombre(txtNombre.getText().toString());
                recomendaciones.get(position).setComentario(txtDesc.getText().toString());
                if (spOpciones.getSelectedItemPosition() == 0) {
                    recomendaciones.get(position).setTipo("restaurante");

                } else {
                    recomendaciones.get(position).setTipo("museo");

                }
                recomendaciones.get(position).setLatitud(latitud);

                recomendaciones.get(position).setLongitud(longitud);
                if (nuevaRecomendacion.getImagenBit().equals("")) {

                } else {
                    recomendaciones.get(position).setImagenBit(nuevaRecomendacion.getImagenBit());
                }
                showToast("Recomendación modificada");
                //recomendaciones.add(nuevaRecomendacion);
            } else {
                GPS();


                nuevaRecomendacion.setNombre(txtNombre.getText().toString());

                nuevaRecomendacion.setComentario(txtDesc.getText().toString());

                if (spOpciones.getSelectedItemPosition() == 0) {
                    nuevaRecomendacion.setTipo("restaurante");

                } else {
                    nuevaRecomendacion.setTipo("museo");

                }
                nuevaRecomendacion.setLatitud(latitud);

                nuevaRecomendacion.setLongitud(longitud);

                //nuevaRecomendacion.setImagenBit(image);


                recomendaciones.add(nuevaRecomendacion);
                showToast("Recomendación creada");

            }
        }
    }
}
