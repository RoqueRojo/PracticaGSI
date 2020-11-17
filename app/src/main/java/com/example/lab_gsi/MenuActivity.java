package com.example.lab_gsi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.lab_gsi.Dominio.Recomendacion;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private ArrayList<Recomendacion> recomendaciones;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        recomendaciones = (ArrayList<Recomendacion>) getIntent().getSerializableExtra("Recomendaciones");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Menú Principal");
    }

    public void crear(View view) {
        Intent i = new Intent(this, CrearActivity.class );
        i.putExtra("Tipo", "restaurantes");
        i.putExtra("Recomendaciones", recomendaciones);
        i.putExtra("Modificar", "no");
        i.putExtra("Recomendacion", recomendaciones.get(0));
        startActivity(i);
    }

    public void listarRestaurantes(View view) {
        Intent intent = new Intent(MenuActivity.this, lista.class);
        intent.putExtra("Tipo", "restaurantes");
        intent.putExtra("Recomendaciones", recomendaciones);
        startActivity(intent);
        finish();
    }

    public void listarMuseos(View view) {
        Intent intent = new Intent(MenuActivity.this, lista.class);
        intent.putExtra("Tipo", "museos");
        intent.putExtra("Recomendaciones", recomendaciones);
        startActivity(intent);
        finish();
    }

    public void modificar(View view) {
        Intent intent = new Intent(MenuActivity.this, lista.class);
        intent.putExtra("Tipo", "modificar");
        System.out.println("Numero recomendaciones(Menu): " +recomendaciones.size());
        intent.putExtra("Recomendaciones", recomendaciones);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
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
}
