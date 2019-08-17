package com.jdcasas.kachuelo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;
import com.jdcasas.kachuelo.modelo.Publicidad;
import com.jdcasas.kachuelo.vista.RecyclerAdapterHorizontal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class Login extends AppCompatActivity   {
    EditText et_usuario,et_password;
    Button ButtonIngresar,ButtonRegistrate;
    Intent intent;
    TextView TextViewOlvidaste;
    ArrayAdapter<String> adaptersp;
    Spinner sp_general;
    ArrayList<String> listItems;
    DialogKachuelo dialogo=new DialogKachuelo( Login.this );
    LinearLayout llv_publicidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_login);
        llv_publicidad = (LinearLayout) findViewById(R.id.llv_publicidad);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
       et_password = (EditText) findViewById(R.id.et_password);
        ButtonIngresar = (Button) findViewById(R.id.ButtonIngresar);
        ButtonRegistrate = (Button) findViewById(R.id.ButtonRegistrate);
        TextViewOlvidaste = (TextView) findViewById(R.id.TextViewOlvidaste);
        //ACCION DE BOTON LOGIN
        ButtonIngresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String usuario=et_usuario.getText().toString();
                String password=et_password.getText().toString();
             //  String  usuario ="995949259";
               //String password ="admin";
            //   String  usuario ="5546556";//usuario empresa
              //  String password ="JY8488";


                if (checklogindata(usuario, password) == true) {//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
                    dialogo.cargarModoUsuario("Usted que usuario es : ",usuario,password );
                } else {//si detecto un error en la primera validacion
                    DialogKachuelo dialogo=new DialogKachuelo( Login.this );
                    dialogo.cargarDialogo("Nombre de usuario o password en blanco");
                }
            }
        });
        ButtonRegistrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialogo.listarOpciones("Registrarse",sp_general,listItems,adaptersp,"Usted es :","spinner_usuario","usuario");
            }
        });
        //SPINNER DEPARTAMENTO o oficios
        sp_general=new Spinner(this);
        listItems = new ArrayList<>();
        adaptersp = new ArrayAdapter<String>(this, R.layout.spinner_departamentos_layout, R.id.txtspdepartamentos, listItems);
        sp_general.setAdapter(adaptersp);
        sp_general.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //////////////////////////////////////////////////
        List<Publicidad> PublicidadList;
        RecyclerView recyclerViewHorizontal;
        recyclerViewHorizontal = (RecyclerView) findViewById(R.id.recycler_view_horizontal);
        recyclerViewHorizontal.setHasFixedSize( true );
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(Login.this, LinearLayoutManager.HORIZONTAL, false));
        PublicidadList=new ArrayList<>(  );
        String urllink="";
        BaseDatos civiltopia = new BaseDatos();
        urllink=civiltopia.spOpciones("publicidad_kachuelo","publicidad");
        loadPublicidad(Login.this,recyclerViewHorizontal,PublicidadList,urllink);
    }

    private void loadPublicidad(final Context context, final RecyclerView recyclerViewHorizontal, final List<Publicidad> PublicidadList , String urllink){
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                PublicidadList.add( new Publicidad(
                                        jsonObject.getString("opciones")
                                ) );
                            }
                            RecyclerAdapterHorizontal adapter_ra=new RecyclerAdapterHorizontal( context,PublicidadList );
                            recyclerViewHorizontal.setAdapter(adapter_ra);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogo.cargarDialogo(""+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(context).add(stringRequest);
        try {
            Thread.sleep( 2000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
            dialogo.cargarDialogo("Error en hilo");
        }
    }

    //validamos si no hay ningun campo en blanco
    public boolean checklogindata(String username ,String password ){
        if 	(username.equals("") || password.equals("")) return false;
        else{ return true; }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Acerca_de:
                dialogo.cargarDialogo( getResources().getString(R.string.mensajeAcercaDe) );
                return true;
            case R.id.Configuracion:
                dialogo.cargarDialogo( "Funcionalidad disponible en la app de paga" );
                return true;
            case R.id.Salir:
                Toast.makeText(getApplicationContext(), "Saliendo.... ", Toast.LENGTH_LONG).show();
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
