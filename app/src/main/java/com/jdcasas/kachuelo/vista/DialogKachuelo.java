package com.jdcasas.kachuelo.vista;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jdcasas.kachuelo.BuscarEmpleo;
import com.jdcasas.kachuelo.PublicarEmpleo;
import com.jdcasas.kachuelo.R;
import com.jdcasas.kachuelo.RegistrarseOficio;
import com.jdcasas.kachuelo.controlaador.BackTaskLogin;
import com.jdcasas.kachuelo.controlaador.GeolocalizarCercaTi;
import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;

import java.util.ArrayList;
public class DialogKachuelo {
    Context  context;
    private String latitud="";
    private String longitud="";
    private String opcion="";
    ArrayAdapter<String> adaptersp;
    Spinner sp_general;
    ArrayList<String> listItems;
    BaseDatos civiltopia = new BaseDatos();
    public DialogKachuelo(Context context){
        this.context=context;
    }
    public void cargarDialogo(String mensaje){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("INFO")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        builder.setIcon( R.drawable.ic_menu_white_24dp);
        builder.create().show();
    }

    public void cargarModosBuscar(String mensaje){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("INFO")
                .setMessage(mensaje)
                .setPositiveButton("Empresa",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, BuscarEmpleo.class);
                                intent.putExtra("opcion","Empresa");
                                context.startActivity(intent);
                                dialog.cancel();
                            }
                        });
        builder.setNegativeButton("EMPLEO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //SPINNER DEPARTAMENTO o oficios
                sp_general=new Spinner(context);
                listItems = new ArrayList<>();
                adaptersp = new ArrayAdapter<String>(context, R.layout.spinner_departamentos_layout, R.id.txtspdepartamentos, listItems);
                sp_general.setAdapter(adaptersp);

               setOpcion( "Empleo" );
                listarOpciones("BuscarEmpleo",sp_general,listItems,adaptersp,"Departamentos :","regions","name");
                dialog.cancel();
            }
        });
        builder.setIcon(R.drawable.ic_menu_white_24dp);
        builder.create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public void cargarDialogoRedes(){
        LinearLayout layoutInputv1 = new LinearLayout(context);
        layoutInputv1.setOrientation(LinearLayout.VERTICAL);
        HorizontalScrollView sv_horizontal=new HorizontalScrollView(context);
        LinearLayout layoutInputh1 = new LinearLayout(context);
        layoutInputh1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout layoutInputvimb1 = new LinearLayout(context);
        layoutInputvimb1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout layoutInputvimb2 = new LinearLayout(context);
        layoutInputvimb2.setOrientation(LinearLayout.VERTICAL);
        LinearLayout layoutInputvimb3 = new LinearLayout(context);
        layoutInputvimb3.setOrientation(LinearLayout.VERTICAL);
        //ImageButton FACEBOOK
        final ImageButton imb_facebook = new ImageButton(context);
        imb_facebook.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook));
        imb_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/groups/423666934321196/about/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse(url));
                context.startActivity(intent);
            }
        });
        //ImageButton TWITTER
        final ImageButton imb_twitter = new ImageButton(context);
        imb_twitter.setImageDrawable(context.getResources().getDrawable(R.drawable.twitter));
        imb_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "imb_twitter", Toast.LENGTH_SHORT).show();
            }
        });
        //ImageButton GMAIL
        final ImageButton imb_gmail = new ImageButton(context);
        imb_gmail.setImageDrawable(context.getResources().getDrawable(R.drawable.gmail));
        imb_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", context.getResources().getString(R.string.mail),null));
                intent.putExtra(Intent.EXTRA_SUBJECT,context.getResources().getString(R.string.subject));
                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.envio)));
            }
        });
        final TextView tv_1 = new TextView(context);
        final TextView tv_2 = new TextView(context);
        final TextView tv_3 = new TextView(context);
        tv_1.setTextSize(13);
        tv_2.setTextSize(13);
        tv_3.setTextSize(13);
        tv_1.setText("Facebook");
        tv_2.setText("Twitter");
        tv_3.setText("Gmail");
        tv_1.setGravity( Gravity.CENTER_HORIZONTAL);
        tv_2.setGravity( Gravity.CENTER_HORIZONTAL);
        tv_3.setGravity( Gravity.CENTER_HORIZONTAL);
        tv_1.setTypeface( Typeface.defaultFromStyle( Typeface.BOLD ) );
        tv_2.setTypeface( Typeface.defaultFromStyle( Typeface.BOLD ) );
        tv_3.setTypeface( Typeface.defaultFromStyle( Typeface.BOLD ) );
        layoutInputvimb1.addView( imb_facebook);
        layoutInputvimb1.addView(tv_1);
        layoutInputvimb2.addView( imb_twitter);
        layoutInputvimb2.addView(tv_2);
        layoutInputvimb3.addView( imb_gmail);
        layoutInputvimb3.addView(tv_3);
        layoutInputh1.addView(layoutInputvimb1);
        layoutInputh1.addView(layoutInputvimb2);
        layoutInputh1.addView(layoutInputvimb3);
        sv_horizontal.addView( layoutInputh1);
        layoutInputv1.addView( sv_horizontal);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layoutInputv1);
        builder.setTitle("INFO")
                .setMessage("")
                .setPositiveButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        builder.setIcon(R.drawable.ic_menu_white_24dp);
        builder.create().show();
    }

    public void listarOpciones(final String actividad, final Spinner sp_general,final  ArrayList<String> listItems,final ArrayAdapter<String> adaptersp, String titulo, final String tabla, final String campo){
        LinearLayout layoutInput = new LinearLayout(context);
        layoutInput.setOrientation(LinearLayout.VERTICAL);
        //TEXTVIEW TITULO
        final TextView tvTitulo = new TextView(context);
        tvTitulo.setTextSize(13);
        tvTitulo.setText("\n\t"+titulo);
        layoutInput.addView(tvTitulo);
        ////SPINNER
        SpinnerLoad cargarOpciones=new SpinnerLoad(listItems,adaptersp,context);
        cargarOpciones.loadSpinner(civiltopia.spOpciones(tabla,campo));
        // SPINNER VACIAR PARA USAR NUEVO
        if(sp_general.getParent() != null) {
            ((ViewGroup)sp_general.getParent()).removeView(sp_general);
        }
        layoutInput.addView(sp_general);
        AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(context);
        builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
        builderEditBiodata.setTitle("OPCIONES");
        builderEditBiodata.setView(layoutInput);
        builderEditBiodata.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                if (actividad.equals("GPSKachuelo"))
                { GeolocalizarCercaTi  geolocalizarCercaTi = null;
                  if(getOpcion().equals( "Publico"))geolocalizarCercaTi=new GeolocalizarCercaTi(context,"usuarios_kachuelo","oficio",latitud,longitud,sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());
                 else if(getOpcion().equals( "Empresa"))geolocalizarCercaTi=new GeolocalizarCercaTi(context,"usuarios_empresa","oficio",latitud,longitud,sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());
                    geolocalizarCercaTi.execute();
                }
                else  if (actividad.equals("GPSUsuarios")) {
                    setOpcion(sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());
                    System.out.println("GPSEmpleadosUsuarios"+sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());

                    if(getOpcion().equals( "Publico")){
                        System.out.println("PUBLICOOOOO");
                        listarOpciones("GPSKachuelo",sp_general,listItems,adaptersp,"Oficios :","spinner_oficio","oficio"); }
                    else if(getOpcion().equals( "Empresa")){
                        System.out.println("EMPRESAAA");
                        listarOpciones("GPSKachuelo",sp_general,listItems,adaptersp,"Rugros :","spinner_rugro","rugro"); }


                }
                else if (actividad.equals("BuscarEmpleo")) {
                    intent = new Intent(context, BuscarEmpleo.class);
                    intent.putExtra("opcion", getOpcion());
                    intent.putExtra("valorsp", sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());
                    context.startActivity(intent);
                }
                else{
                    if (actividad.equals("Registrarse")) intent = new Intent(context, RegistrarseOficio.class);
                    else if (actividad.equals("PublicarEmpleo")) intent = new Intent(context, PublicarEmpleo.class);
                    intent.putExtra("valorsp", sp_general.getItemAtPosition(sp_general.getSelectedItemPosition()).toString());
                    context.startActivity(intent);
                }
            }
        });
        builderEditBiodata.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderEditBiodata.show();
    }

    public void cargarModoUsuario(String mensaje, final String usuario, final String password){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("INFO")
                .setMessage(mensaje)
                .setPositiveButton("Empresa",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackTaskLogin conexlogin=new BackTaskLogin(context,"usuarios_empresa",usuario,password);
                                conexlogin.execute();
                                dialog.cancel();
                            }
                        });
        builder.setNegativeButton("Publico", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BackTaskLogin conexlogin=new BackTaskLogin(context,"usuarios_kachuelo",usuario,password);
                conexlogin.execute();
                dialog.cancel();
            }
        });
        builder.setIcon(R.drawable.ic_menu_white_24dp);
        builder.create().show();
    }


    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
}
