package com.jdcasas.kachuelo.vista;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jdcasas.kachuelo.modelo.Publicidad;
import com.jdcasas.kachuelo.R;

import java.util.List;
public  class RecyclerAdapterHorizontal extends RecyclerView.Adapter<RecyclerAdapterHorizontal.ViewHolder> {
    final  Context context;
    final  List<Publicidad> PublicidadList;
    public RecyclerAdapterHorizontal( Context context,  List<Publicidad> PublicidadList) {
        this.context = context;
        this.PublicidadList = PublicidadList;
    }
    @Override
    //iniciaqliza la interfaz de la vista
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate( R.layout.cv_h_publicidad,null );
        return new ViewHolder(view);
    }
    @Override
    //Se actualiza los campos de acuerdo a la posicion
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Publicidad  avisos=PublicidadList.get(posicion  );
        Glide.with(context)
                .load(avisos.getPublicidad())
                .error( R.drawable.camara )
                .into(holder.item_image);
        holder.tv_item1.setText("\nVer Ofertas\n\n");
    }
    @Override
    //nos devuelve el tamaño de la lista
    public int getItemCount() {
        return PublicidadList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView item_image;
        public TextView tv_item1;
        public ViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView)itemView.findViewById(R.id.item_image);
            tv_item1 = (TextView)itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    DialogKachuelo dialogo=new DialogKachuelo( context);
                    dialogo.cargarDialogo( "Funcionalidad disponible en la app de paga" );
                   // Snackbar.make(v, "Click detected on item " + position,
                        //    Snackbar.LENGTH_LONG)
                         //   .setAction("Action", null).show();
                }
            });
        }
    }
}
