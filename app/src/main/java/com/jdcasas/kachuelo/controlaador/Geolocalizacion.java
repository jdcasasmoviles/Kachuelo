package com.jdcasas.kachuelo.controlaador;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;

public class Geolocalizacion {
    EditText et_latitud,et_longitud;
    private String latitud;
    private String longitud;
    Context context;
    private LocationListener locListener;
    private LocationManager locManager;
    private  int MY_PERMISSIONS_REQUEST_READ_CONTACTS  ;
    String celular;
    int control=0;
    ArrayAdapter<String> adaptersp;
    Spinner sp_general;
    ArrayList<String> listItems;
    DialogKachuelo dialogo;
    String tabla;
    public Geolocalizacion(Context context, String celular, final Spinner sp_general, final ArrayList<String> listItems, final ArrayAdapter<String> adaptersp){
        this.context=context;
        this.celular=celular;
        this.sp_general=sp_general;
        this.listItems=listItems;
        this.adaptersp=adaptersp;
        this.control=2;///////para GPSKACHUELO
    }
    public Geolocalizacion(Context context, EditText et_latitud,EditText et_longitud){
        this.context=context;
        this.et_latitud=et_latitud;
        this.et_longitud=et_longitud;
        this.control=1;//PARA REGISTRAR
    }
    public Geolocalizacion(Context context,String tabla){
        this.context=context;
        this.tabla=tabla;//PARA MAPA TIEMPO REAL
    }

    ////////////////////////////////OBTENER COORDENAS/////////////////////////////////////////////////
    private void mostrarPosicion(Location loc) {
        if(loc != null) {
            setLatitud( String.valueOf(loc.getLatitude()));
            setLongitud(String.valueOf(loc.getLongitude()));
        }else {
            setLatitud( "sin datos" );
            setLongitud("sin datos");
        }
    }

    public  void comenzarLocalizacion() {
        locManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( (Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarPosicion(loc);
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) { mostrarPosicion(location); }
            public void onProviderDisabled(String provider) {  }
            public void onProviderEnabled(String provider) {  }
            public void onStatusChanged(String provider, int status, Bundle extras) {  }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
        if(getLatitud().equals("sin datos") || getLongitud().equals("sin datos")  || getLatitud().equals("") || getLongitud().equals("")   ) {
            Toast.makeText(context, "ACTIVA TU GPS", Toast.LENGTH_SHORT).show();
        }
        else if(!getLatitud().equals("sin datos") && !getLongitud().equals("sin datos")  && !getLatitud().equals("") && !getLongitud().equals("") )
        {   if(control==1){
            et_latitud.setText( getLatitud() );
            et_longitud.setText( getLongitud() );
            }else if(control==2){
            setLatitud( latitud );
            setLongitud( longitud);
            dialogo=new DialogKachuelo( context );
            dialogo.setLongitud( getLongitud() );
            dialogo.setLatitud( getLatitud() );
            dialogo.listarOpciones("GPSUsuarios",sp_general,listItems,adaptersp,"Que desea ubicar :","spinner_usuario","usuario");

        }else  countDownTimer();
            Toast.makeText(context, "LISTO CONTINUA", Toast.LENGTH_SHORT).show();
        }
    }
    ///////////Se ejecuta cada 30 min y actualiza la localizacion del dispositivo
     //1 segundos=1 000    30min=1800segundos=1800000
    private void countDownTimer(){
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                //////ACTUALIZA COOR GPS
                BaseDatos civiltopia=new BaseDatos( );
                String link=civiltopia.nuevaPosicion("usuarios_kachuelo",celular,getLatitud(),getLongitud());
                updateGPS(link);
                countDownTimer();
            }
        }.start();
    }

    public  void updateGPS(String urllink){
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                System.out.println("UPDATEEEEEE  "+jsonObject.getString("resultado"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(context).add(stringRequest);
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
}
