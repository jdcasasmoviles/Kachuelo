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
import com.jdcasas.kachuelo.R;
import com.jdcasas.kachuelo.modelo.Empresas;

import java.util.List;
public class RecyclerAdapterEmpresas extends RecyclerView.Adapter<RecyclerAdapterEmpresas.ViewHolder> {
    Context context;
    List<Empresas> EmpresasList;
    public RecyclerAdapterEmpresas(Context context, List<Empresas>  EmpresasList) {
        this.context = context;
        this.EmpresasList = EmpresasList;
    }
    @Override
    //iniciaqliza la interfaz de la vista
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate( R.layout.cv_v_empresas,null );
        return new ViewHolder(view);
    }
    @Override
    //Se actualiza los campos de acuerdo a la posicion
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Empresas empresarios=EmpresasList.get(posicion  );
        Glide.with(context)
                .load(empresarios.getImagenEmpresas())
                .error( R.drawable.camara )
                .into(holder.item_image);
        holder.tv_item1.setText(empresarios.getNombre() );
        holder.tv_item2.setText( empresarios.getDescripcion() );
        holder.tv_item3.setText( empresarios.getCelular());
        holder.tv_item4.setText(empresarios.getCoorx() );
        holder.tv_item5.setText( empresarios.getCoory());
    }
    @Override
    //nos devuelve el tamaño de la lista
    public int getItemCount() {
        return EmpresasList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView item_image;
        public TextView tv_item1;
        public TextView tv_item2;
        public TextView tv_item3;
        public TextView tv_item4;
        public TextView tv_item5;
        public ViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView)itemView.findViewById(R.id.item_image);
            tv_item1 = (TextView)itemView.findViewById(R.id.tv_item1);
            tv_item2 = (TextView)itemView.findViewById(R.id.tv_item2);
            tv_item3 = (TextView)itemView.findViewById(R.id.tv_item3);
            tv_item4 = (TextView)itemView.findViewById(R.id.tv_item4);
            tv_item5 = (TextView)itemView.findViewById(R.id.tv_item5);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }
    }
}
