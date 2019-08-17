package com.jdcasas.kachuelo;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jdcasas.kachuelo.controlaador.Geolocalizacion;
import com.jdcasas.kachuelo.vista.CustomGrid;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    GridView grid;
    String[] web = {
            "Salir",
            "Compartir",
            "Proyectos" ,
            "GPSKachuelo",
            "Buscar",
            "PublicarEmpleo",
            "Web",
            "Acercade"
    } ;
    int[] imageId = {
            R.drawable.q1,
            R.drawable.q2,
            R.drawable.q4,
            R.drawable.q3,
            R.drawable.q5,
            R.drawable.q6,
            R.drawable.kachuelo,
            R.drawable.about
    };
    ArrayAdapter<String> adaptersp;
     Spinner sp_general;
    ArrayList<String> listItems;
    String campo1="",campo2="",campo3="";
    private String latitud="";
    private String longitud="";
    Bundle extras;
    DialogKachuelo dialogo=new DialogKachuelo( MainActivity.this );
    TextView TextViewUsuarioKacgeulo;
    private LocationListener locListener;
    private LocationManager locManager;
    private  int MY_PERMISSIONS_REQUEST_READ_CONTACTS  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       recibeDatosActividad();//PROCESO PARA OBTENER DATOS DE OTRO ACTIVIDAD
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
        ////////////////////////GRID/////////////////////////////////////
        CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter( adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "Click " +web[+ position], Toast.LENGTH_SHORT).show();
                Intent intent;
                if(web[+ position].equals("Salir")){ finish(); }
                else if(web[+ position].equals("Compartir")){
                   Intent  i=new Intent(MainActivity.this,CompartirProyecto.class);
                    i.putExtra("campo1", campo1);
                    i.putExtra("campo2", campo2);
                    i.putExtra("campo3", campo3);
                    startActivity(i);
              }
                else if(web[+ position].equals("Proyectos")){
                    Intent i=new Intent(MainActivity.this,FeedProyectos.class);
                    i.putExtra("campo1", campo1);
                    i.putExtra("vitacora"," proyectos");
                    startActivity(i);
                }
                else if(web[+ position].equals("GPSKachuelo")){
                    Geolocalizacion obtenerGPS=new Geolocalizacion(MainActivity.this,campo2,sp_general,listItems,adaptersp);
                    obtenerGPS.comenzarLocalizacion();
                }
                else if(web[+ position].equals("Buscar")){
                    dialogo.cargarModosBuscar( "Desea buscar" );
                }
                else if(web[+ position].equals("PublicarEmpleo")){
                    dialogo. listarOpciones("PublicarEmpleo",sp_general,listItems,adaptersp,"Departamentos :","regions","name");
                }
                else if(web[+ position].equals("Web")){
                    dialogo.cargarDialogo( "Funcionalidad disponible en la app de paga" );
                }
                else if(web[+ position].equals("Acercade")){
                    intent = new Intent(MainActivity.this, Acerca.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void recibeDatosActividad(){
         extras = getIntent().getExtras();
        if (extras != null) {
            campo1  = extras.getString("campo1");//nombre
            campo2  = extras.getString("campo2");//celular
            campo3  = extras.getString("campo3");//oficio
        }else{
            campo1  ="Usuario";
            campo3  ="Kachuelo";
        }
        ///////////////NAVEGATION VIEW /////////////////
        drawerLayout = (DrawerLayout)
                findViewById(R.id.navigation_drawer_usuario_K);
        navigationView = (NavigationView)
                findViewById(R.id.navigation_view_usuario_k);
        if (navigationView != null) {
            setupNavigation(navigationView);
        }
        //TEXTVIEW DE NAVEGATION VIEW
        View header=navigationView.getHeaderView(0);
        TextViewUsuarioKacgeulo= (TextView)header.findViewById(R.id.TextViewUsuarioKacgeulo);
        TextViewUsuarioKacgeulo.setText("Bienvenido :\n"+campo1+"\n"+campo3 );
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
    ///////////////////////NAVEGATION VIEW//////////////////////////////////////////////////////////////////////////////
    public void setupNavigation(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_1:
                                menuItem.setChecked(true);
                                dialogo.cargarDialogo( "Funcionalidad disponible en la app de paga" );              return true;
                            case R.id.item_navigation_2:
                                menuItem.setChecked(true);
                                dialogo.cargarDialogo( "Funcionalidad disponible en la app de paga" );
                                return true;
                            case R.id.item_navigation_3:
                                menuItem.setChecked(true);
                                Intent  i=new Intent(MainActivity.this,FeedProyectos.class);
                                i.putExtra("campo1", campo1);//nombre
                                i.putExtra("vitacora", "vitacora");//vitacora
                                startActivity(i);
                                return true;
                            case R.id.item_navigation_4:
                                menuItem.setChecked(true);
                                dialogo.cargarDialogoRedes( );
                                return true;
                            case R.id.item_navigation_5:
                                menuItem.setChecked(true);
                                dialogo.cargarDialogo( getResources().getString(R.string.terminos) );
                                return true;
                        }
                        return true;
                    }
                });
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
