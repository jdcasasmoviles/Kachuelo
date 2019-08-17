package com.jdcasas.kachuelo.controlaador;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdcasas.kachuelo.modelo.Proyectos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;
import com.jdcasas.kachuelo.vista.RecyclerAdapterProyectos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SpinnerLoad {
    Context context;
    ArrayList<String> list;
    String error="";
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
     DialogKachuelo dialogo;
    public SpinnerLoad(ArrayList<String> listItems,ArrayAdapter<String> adapter,Context context) {
        this.listItems=listItems;
        this.adapter=adapter;
        this.context=context;
        dialogo= new DialogKachuelo( context );
    }

    public SpinnerLoad(Context context) {
    this.context=context;
        dialogo= new DialogKachuelo( context );
    }

    public  void loadSpinner(String urllink){
        list=new ArrayList<>();
        listItems.clear();
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                list.add(jsonObject.getString("opciones"));
                            }
                            listItems.addAll(list);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogo.cargarDialogo( "ERROR JSON" );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogo.cargarDialogo( "ERROR SPINNER" );
                    }
                });
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public  void loadInsertarProyectos(String urllink){
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            String mensaje="Gracias por compartir tu proyecto";
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                if(jsonObject.optString("resultado").equals("vacio")){
                                    mensaje="NO SE INSERTO PROYECTO";
                                    break;}
                            }
                            dialogo.cargarDialogo( mensaje );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogo.cargarDialogo( "ERROR  JSON" );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogo.cargarDialogo( "ERROR PROYECTO" );
                    }
                });
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public  void loadProyectos(final RecyclerView recyclerView, String urllink){
        final List<Proyectos>  ProyectosList=new ArrayList<>(  );
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                ProyectosList.add( new Proyectos(
                                        jsonObject.optString("nombreProyecto"),
                                        jsonObject.optString("descripcion"),
                                        jsonObject.optString("creadoPor"),
                                        jsonObject.optString("imagen1"),
                                        jsonObject.optString("imagen2")
                                ) );
                            }
                            RecyclerAdapterProyectos adapter_ra=new RecyclerAdapterProyectos( context,ProyectosList );
                            recyclerView.setAdapter(adapter_ra);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogo.cargarDialogo( "ERROR  JSON" );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogo.cargarDialogo( "ERROR PROYECTO" );
                    }
                });
        Volley.newRequestQueue(context).add(stringRequest);
    }

}
