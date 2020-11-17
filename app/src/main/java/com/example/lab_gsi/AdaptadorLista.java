package com.example.lab_gsi;

import android.content.Context;
//import android.support.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.example.lab_gsi.Dominio.Recomendacion;
import com.example.lab_gsi.R;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {
    private ArrayList<Recomendacion> recomendaciones;
    private Context context;
    private OnItemSelectedListener itemSelectedListener;

    public interface OnItemSelectedListener{
        void mostrarInfo(Recomendacion r);
        void Gps(Recomendacion r);
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




        ViewHolder(View view) {
            super(view);
            imgLugar = view.findViewById(R.id.imgLugar);
            imageView4 = view.findViewById(R.id.imageView4);
            btnGps = view.findViewById(R.id.btnGPS);
            imgTipoLugar = view.findViewById(R.id.imgTipoLugar);
            lblNombreLugar = view.findViewById(R.id.lblNombreLugar);
            lblDes = view.findViewById(R.id.lblDes);
            view.setOnClickListener(this);
            btnGps.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int posicion = getAdapterPosition();
            if (itemSelectedListener != null) {
                //itemSelectedListener.mostrarInfo(recomendaciones.get(posicion));
                itemSelectedListener.Gps(recomendaciones.get(posicion));
            }
        }
    }
    public AdaptadorLista(Context context, ArrayList<Recomendacion> recomendaciones){
        this.context = context;
        this.recomendaciones = recomendaciones;
    }

    @Override
    public AdaptadorLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(AdaptadorLista.ViewHolder holder, int position) {
        Recomendacion recomendacion = recomendaciones.get(position);
        //Picasso.with(context).load(recomendacion.getImagenBit()).into(holder.imgLugar);
        //holder.imgLugar.setImageResource();


        Uri uri = Uri.parse(recomendaciones.get(position).getImagenBit());

        System.out.println("Uri: " + uri);
        Picasso.with(context).load(uri).into(holder.imgLugar);
        //System.out.println("Imprimir: " + Uri.parse(recomendaciones.get(position).getImagenBit()));
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