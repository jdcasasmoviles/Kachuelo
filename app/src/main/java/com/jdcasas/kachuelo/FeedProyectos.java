package com.jdcasas.kachuelo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

public class FeedProyectos extends AppCompatActivity {
    Bundle extras;
    public DialogKachuelo dialogo;
    RecyclerView recyclerView;
    String campo1="",vitacora="";
    BaseDatos civiltopia=new BaseDatos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feed_proyectos );
        recibeDatosActividad();
        dialogo=new DialogKachuelo( FeedProyectos.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_proyectos);
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager(new LinearLayoutManager( FeedProyectos.this ));
        SpinnerLoad cargarProyectos=new SpinnerLoad(FeedProyectos.this);
       if(vitacora.equals( "vitacora" )) cargarProyectos.loadProyectos( recyclerView,civiltopia.vitacora(campo1));
       else  cargarProyectos.loadProyectos( recyclerView,civiltopia.llenarProyectos());//feed
    }

    public void recibeDatosActividad(){
        extras = getIntent().getExtras();
        if (extras != null) {
            campo1  = extras.getString("campo1");//nombre
            vitacora  = extras.getString("vitacora");//vitacora
            System.out.println("xxxxxxxxxxxxxxxxxxxxx         "+vitacora);
        }else{
           finish();
        }
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
