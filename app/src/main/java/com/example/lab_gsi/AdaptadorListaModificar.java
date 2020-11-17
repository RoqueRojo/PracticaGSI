package com.example.lab_gsi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_gsi.Dominio.Recomendacion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class AdaptadorListaModificar extends RecyclerView.Adapter<AdaptadorListaModificar.ViewHolder>  {
    private ArrayList<Recomendacion> recomendaciones;
    private Context context;
    private OnItemSelectedListener itemSelectedListener;

    public interface OnItemSelectedListener {
        void Modificar(Recomendacion r);
        void Borrar (int position);
    }

    public void setItemSelectedListener(OnItemSelectedListener itemSelectedListener)
    {
        this.itemSelectedListener = itemSelectedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgLugar;
        private ImageView imageView4;
        private Button btnGps;
        private ImageView imgTipoLugar;
        private TextView lblNombreLugar;
        private TextView lblDes;
        private Button btnBorrar;
        private Button btnModificar;




        ViewHolder(View view) {
            super(view);
            imgLugar = view.findViewById(R.id.imgLugar);
            imageView4 = view.findViewById(R.id.imageView4);
            btnGps = view.findViewById(R.id.btnGPS);
            imgTipoLugar = view.findViewById(R.id.imgTipoLugar);
            lblNombreLugar = view.findViewById(R.id.lblNombreLugar);
            lblDes = view.findViewById(R.id.lblDes);
            btnBorrar = view.findViewById(R.id.btnBorrar);
            btnModificar = view.findViewById(R.id.btnModificar);
            view.setOnClickListener(this);

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if (itemSelectedListener != null) {
                        itemSelectedListener.Borrar(posicion);
                    }



                }
            });

        }

        @Override
        public void onClick(View v) {
            int posicion = getAdapterPosition();
            if (itemSelectedListener != null) {
                itemSelectedListener.Modificar(recomendaciones.get(posicion));
            }
        }
    }
    public AdaptadorListaModificar(Context context, ArrayList<Recomendacion> recomendaciones){
        this.context = context;
        this.recomendaciones = recomendaciones;
    }
    @Override
    public AdaptadorListaModificar.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_modificar, parent, false);
        return new AdaptadorListaModificar.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorListaModificar.ViewHolder holder, int position) {
        Recomendacion recomendacion = recomendaciones.get(position);


        Uri uri = Uri.parse(recomendaciones.get(position).getImagenBit());
        Picasso.with(context).load(uri).into(holder.imgLugar);

        //holder.imgLugar.setImageURI(Uri.parse(recomendaciones.get(position).getImagenBit()));
        holder.lblNombreLugar.setText(recomendaciones.get(position).getNombre());
        holder.lblDes.setText(recomendaciones.get(position).getComentario());

        switch (recomendaciones.get(position).getTipo())
        {
            case "restaurante": //Cargar imagen de lugares tipo "restaurante"
                holder.imgTipoLugar.setImageResource(R.drawable.food);
                break;
            case "museo": //Cargar imagen de los lugares tipo "museos"
                holder.imgTipoLugar.setImageResource(R.drawable.museum);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return recomendaciones.size();
    }
}
