package com.jdcasas.kachuelo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.modelo.Empleos;
import com.jdcasas.kachuelo.modelo.Empresas;
import com.jdcasas.kachuelo.vista.DialogKachuelo;
import com.jdcasas.kachuelo.vista.RecyclerAdapter;
import com.jdcasas.kachuelo.vista.RecyclerAdapterEmpresas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class BuscarEmpleo extends AppCompatActivity {
    String departamento="";
    ArrayList<String> listOficio= new ArrayList<>();
    ArrayList<String> listProvincia= new ArrayList<>();
    ArrayList<String> listDistrito= new ArrayList<>();
    ArrayAdapter<String> adapterOficio,adapterProvincia,adapterDistrito;
    Spinner sp_oficio,sp_provincia,sp_distrito;
    Button bt_buscar,ButtonVolver;
    DialogKachuelo dialogo=new DialogKachuelo(BuscarEmpleo.this);
    private static String urllink="";
    List<Empleos> EmpleosList;
    List<Empresas> EmpresasList;
    RecyclerView recyclerView;
    LinearLayout llh_buscar_empresa,llh_buscar_empleo;
    TextView tw_buscar;
    BaseDatos civiltopia = new BaseDatos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_buscar_empleo );
        //LINEAR LAYOUT
        this.llh_buscar_empleo = (LinearLayout) findViewById(R.id.llh_buscar_empleo);
        this.llh_buscar_empresa = (LinearLayout) findViewById(R.id.llh_buscar_empresa);
        //TEXTVIEW  DEL TITULO DE LA ACTIVIDAD
        this.tw_buscar = (TextView)findViewById(R.id.tw_buscar);
        //BOTONES
        this.bt_buscar = (Button) findViewById(R.id.bt_buscar);
        this.ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
        //PROCESO PARA OBTENER DATOS
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ////Empleo
            if(extras.getString("opcion").equals( "Empleo" )){
                llh_buscar_empresa.removeAllViews();
                llh_buscar_empleo.setVisibility( View.VISIBLE );
                departamento  = extras.getString("valorsp");
                this.tw_buscar.setText("Buscar Empleo en  " + departamento.toUpperCase());
                //SPINNER  DISTRITO
                this.sp_distrito = (Spinner) findViewById(R.id.sp_distrito);
                adapterDistrito = new ArrayAdapter<String>(this, R.layout.spinner_distritos_layout, R.id.txtspdistritos, listDistrito);
                sp_distrito.setAdapter(adapterDistrito);
                sp_distrito.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                //SPINNER  PROVINCIA
                this.sp_provincia = (Spinner) findViewById(R.id.sp_provincia);
                adapterProvincia = new ArrayAdapter<String>(this, R.layout.spinner_provincias_layout, R.id.txtspprovincias, listProvincia);
                sp_provincia.setAdapter(adapterProvincia);
                sp_provincia.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                String provincia=sp_provincia.getItemAtPosition(sp_provincia.getSelectedItemPosition()).toString();
                                SpinnerLoad cargarDistritos=new SpinnerLoad(listDistrito,adapterDistrito,BuscarEmpleo.this );
                                cargarDistritos.loadSpinner( civiltopia.listaDepasDistritos( departamento,provincia ) );
                                sp_distrito.setAdapter(adapterDistrito);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                //SPINNER  OFICIO
                this.sp_oficio = (Spinner) findViewById(R.id.sp_oficio);
                adapterOficio = new ArrayAdapter<String>(this, R.layout.spinner_oficio_layout, R.id.txtspoficio, listOficio);
                sp_oficio.setAdapter(adapterOficio);
                ///////////////////LLENAR SPINNER DESDE RED///////////////////////////////
                SpinnerLoad cargarOficios=new SpinnerLoad( listOficio,adapterOficio,BuscarEmpleo.this );
                cargarOficios.loadSpinner(civiltopia.spOpciones( "spinner_oficio","oficio" ));
                SpinnerLoad  cargarProvincias=new SpinnerLoad(listProvincia,adapterProvincia,BuscarEmpleo.this);
                cargarProvincias.loadSpinner(civiltopia.listarDepasProvincias(departamento));
            }
            ////EMPRESA
            else if(extras.getString("opcion").equals( "Empresa" )){
                llh_buscar_empleo.removeAllViews();
                llh_buscar_empresa.setVisibility( View.VISIBLE );
                this.tw_buscar.setText("Buscar empresa");
                //SPINNER  RUGRO
                this.sp_oficio = (Spinner) findViewById(R.id.sp_oficio_empresa);
                adapterOficio = new ArrayAdapter<String>(this, R.layout.spinner_oficio_layout, R.id.txtspoficio, listOficio);
                sp_oficio.setAdapter(adapterOficio);
                SpinnerLoad cargarOficios=new SpinnerLoad( listOficio,adapterOficio,BuscarEmpleo.this );
                cargarOficios.loadSpinner(civiltopia.spOpciones( "spinner_rugro","rugro" ));
            }
        }
        else  finish();
        sp_oficio.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //////////////////////////////////////BOTONES////////////////////////////////////////////////////
        //BOTON BUSCAR
        bt_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_buscar);
                recyclerView.setHasFixedSize( true );
                recyclerView.setLayoutManager(new LinearLayoutManager( BuscarEmpleo.this ));
                BaseDatos civiltopia = new BaseDatos();
                EmpleosList=new ArrayList<>(  );
                if(extras.getString("opcion").equals( "Empleo" )){
                    String valor1=sp_provincia.getItemAtPosition(sp_provincia.getSelectedItemPosition()).toString();
                    String valor2=sp_distrito.getItemAtPosition(sp_distrito.getSelectedItemPosition()).toString();
                    String valor3=sp_oficio.getItemAtPosition(sp_oficio.getSelectedItemPosition()).toString();
                    urllink=civiltopia.lwBuscar(departamento,valor1,valor2,valor3,"provincia","distrito","oficio");
                    loadEmpleados(urllink);
                }
                else if(extras.getString("opcion").equals( "Empresa" )){
                    String valor1=sp_oficio.getItemAtPosition(sp_oficio.getSelectedItemPosition()).toString();
                    urllink=civiltopia.buscarEmpresas("usuarios_empresa",valor1,"oficio");
                    EmpresasList=new ArrayList<>(  );
                    loadEmpresas(urllink);
                }
            }
        });
        //BOTON VOLVER
        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onStart(){
        super.onStart();

    }

    private void loadEmpresas(String urllink){
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                EmpresasList.add( new Empresas(
                                        jsonObject.getString("e1"),//nombre
                                        jsonObject.getString("e2"),//descripcion
                                        jsonObject.getString("e3"),//celular
                                        jsonObject.getString("e4"),//coorx
                                        jsonObject.getString("e5"),//coory
                                        jsonObject.getString("e6")//imagen
                                ) );
                            }
                            RecyclerAdapterEmpresas adapter_ra=new RecyclerAdapterEmpresas( BuscarEmpleo.this,EmpresasList );
                            recyclerView.setAdapter(adapter_ra);
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadEmpleados(String urllink){
        StringRequest stringRequest=new StringRequest( Request.Method.GET,urllink,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray =new JSONArray(response);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=jArray.getJSONObject(i);
                                // add interviewee name to arraylist
                                EmpleosList.add( new Empleos(
                                        jsonObject.getString("c4"),
                                        jsonObject.getString("c2"),
                                        jsonObject.getString("c1"),
                                        jsonObject.getString("c3"),
                                        jsonObject.getString("c5")
                                ) );
                            }
                            RecyclerAdapter adapter_ra=new RecyclerAdapter( BuscarEmpleo.this,EmpleosList );
                            recyclerView.setAdapter(adapter_ra);
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
        Volley.newRequestQueue(this).add(stringRequest);
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
