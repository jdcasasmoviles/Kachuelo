package com.jdcasas.kachuelo.vista;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jdcasas.kachuelo.R;
import com.jdcasas.kachuelo.modelo.Proyectos;

public class RecyclerAdapterProyectos extends RecyclerView.Adapter<RecyclerAdapterProyectos.ViewHolder> {
    Context context;
    List<Proyectos> ProyectosList;
    public RecyclerAdapterProyectos(Context context, List<Proyectos>  ProyectosList) {
        this.context = context;
        this.ProyectosList = ProyectosList;
    }
    @Override
    //iniciaqliza la interfaz de la vista
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate( R.layout.card_view_proyectos,null );
        return new ViewHolder(view);
    }
    @Override
    //Se actualiza los campos de acuerdo a la posicion
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Proyectos  proyectosMuchos=ProyectosList.get(posicion  );
        cargaImagenes( proyectosMuchos.getImagen1(),holder.item_image1  );
        cargaImagenes( proyectosMuchos.getImagen2(),holder.item_image2  );
        holder.tv_item1.setText(proyectosMuchos.getNombreProyecto());
        holder.tv_item2.setText(proyectosMuchos.getDescripcion());
        holder.tv_item3.setText( proyectosMuchos.getCreadoPor());
    }

   public void cargaImagenes(final String linkImagen, final ImageView iv_proyecto){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReferenceFromUrl("gs://kachuelo-d6c23.appspot.com");
        mStorageRef.child(linkImagen ).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .centerCrop()
                        .error( R.drawable.camara )
                        .into(iv_proyecto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ProyectosList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView item_image1;
        public ImageView item_image2;
        public TextView tv_item1;
        public TextView tv_item2;
        public TextView tv_item3;
        public ViewHolder(View itemView) {
            super(itemView);
            item_image1 = (ImageView)itemView.findViewById(R.id.item_image1);
            item_image2= (ImageView)itemView.findViewById(R.id.item_image2);
            tv_item1 = (TextView)itemView.findViewById(R.id.tv_item1);
            tv_item2 = (TextView)itemView.findViewById(R.id.tv_item2);
            tv_item3 = (TextView)itemView.findViewById(R.id.tv_item3);
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
