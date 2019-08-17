package com.jdcasas.kachuelo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jdcasas.kachuelo.controlaador.BackTaskAgregar;
import com.jdcasas.kachuelo.controlaador.Geolocalizacion;
import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import java.util.Random;
public class RegistrarseOficio extends AppCompatActivity {
    EditText et_longitud,et_latitud,et_descripcion,et_celular,et_nombreapellido;
    TextView tw_registrarse,tw_opcion,tw_nombreapellido;
    Spinner sp_oficio;
    Button bt_registrar,ButtonReset,ButtonVolver,btnLlenarsGPS;
    ArrayList<String> listOficio= new ArrayList<>();
    ArrayAdapter<String> adapterOficio;
    DialogKachuelo dialogo;
    String valorsp="";
    Bundle extras;
    BaseDatos civiltopia = new BaseDatos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registrarse_oficio );
        //Obtenemos datos enviados
        extras = getIntent().getExtras();
        if (extras != null) {
            valorsp = extras.getString( "valorsp" );
        }
        //EDITTEXT
        this.tw_registrarse = (TextView) findViewById(R.id.tw_registrarse);
        this.tw_opcion = (TextView) findViewById(R.id.tw_opcion);
        this.tw_nombreapellido = (TextView) findViewById(R.id.tw_nombreapellido);
        this.et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        this.et_nombreapellido = (EditText) findViewById(R.id.et_nombreapellido);
        this. et_celular= (EditText) findViewById(R.id.et_celular);
        this.et_longitud = (EditText) findViewById(R.id.et_longitud);
        this.et_latitud= (EditText) findViewById(R.id.et_latitud);
        //BOTON
        this.bt_registrar= (Button) findViewById(R.id.bt_registrar);
        this.ButtonReset = (Button) findViewById(R.id.ButtonReset);
        this.ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
        this.btnLlenarsGPS = (Button) findViewById(R.id.btnLlenarsGPS);
        //TITULO
        tw_registrarse.setText("Registrarse "+valorsp);
        //SPINNER  OFICIO
        this.sp_oficio = (Spinner) findViewById(R.id.sp_oficio);
        adapterOficio = new ArrayAdapter<String>(this, R.layout.spinner_oficio_layout, R.id.txtspoficio, listOficio);
        sp_oficio.setAdapter(adapterOficio);
        sp_oficio.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listItems.get(position), Toast.LENGTH_LONG).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //BOTON ACTUALIZAR GPS O ACTIVAR
        btnLlenarsGPS = (Button) findViewById(R.id.btnLlenarsGPS);
        btnLlenarsGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geolocalizacion obtenerGPS=new Geolocalizacion( RegistrarseOficio.this,et_latitud,et_longitud );
                obtenerGPS.comenzarLocalizacion();
            }
        });
        //BOTON PARA REGISTRAR
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final String[] datos = new String[7];
                    datos[0]= et_nombreapellido.getText().toString();
                    datos[1]= et_celular.getText().toString();
                    datos[2]=sp_oficio.getItemAtPosition(sp_oficio.getSelectedItemPosition()).toString();
                    datos[3]= et_descripcion.getText().toString();
                    datos[4]= et_latitud.getText().toString();//coor  x  latitud;
                    datos[5]= et_longitud.getText().toString();//coor y  longitud
                    datos[6]=generaPassword(6);
                    dialogoRegistrar(datos);
            }
        });
        //BOTON RESET
        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nombreapellido.setText("");
                et_celular.setText("");
                et_descripcion.setText("");

            }
        });
        //BOTON VOLVER
        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ///////////////////////LLENADO DE FORMULARIO EMPRESA O PUBLICO
        if(valorsp.equals("Publico")) {tw_opcion.setText( "Oficio" );
            tw_nombreapellido.setText( "Nombre y apellido" );
         //   cargarOpciones = new BackTaskSpinner( listOficio, adapterOficio, "spinner_oficio", "oficio", RegistrarseOficio.this );
            SpinnerLoad cargarOficios=new SpinnerLoad( listOficio,adapterOficio,RegistrarseOficio.this );
            cargarOficios.loadSpinner(civiltopia.spOpciones( "spinner_oficio","oficio" ));

        }else if(valorsp.equals("Empresa")) {tw_opcion.setText( "Rugro" );
            tw_nombreapellido.setText( "Nombre de empresa" );
          //  cargarOpciones = new BackTaskSpinner( listOficio, adapterOficio, "spinner_rugro", "rugro", RegistrarseOficio.this );
            SpinnerLoad cargarOficios=new SpinnerLoad( listOficio,adapterOficio,RegistrarseOficio.this );
            cargarOficios.loadSpinner(civiltopia.spOpciones( "spinner_rugro","rugro" ));
        }//cargarOpciones.execute();
    }
    //SE EJECUTA PARA CARGAR LOS SPINNERS
    public void onStart(){
        super.onStart();
    }

    ////////////////////METODO PARA GENERAR PASSWORD//////////////////////////////////////
    public String generaPassword (int longitud){
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i < longitud){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                cadenaAleatoria += c;
                i ++;
            }
        }
        return cadenaAleatoria;
    }
    //////////////////////////METODO PARA REGISTRAR////////////////////////////////////////////////////
    public void dialogoRegistrar(final String[] datos){
        if(!datos[0].equals("")&& !datos[1].equals("")  && !datos[2].equals("") && !datos[3].equals("") && !datos[4].equals("") && !datos[5].equals("")&& !datos[4].equals("sin datos") && !datos[5].equals("sin datos")){
            String cadenaProyecto="\n";
            cadenaProyecto=cadenaProyecto+"NOMBRE COMPLETO\t: "+datos[0]+"\n\n";
            cadenaProyecto=cadenaProyecto+"CELULAR\t: "+datos[1]+"\n\n";
            if(valorsp.equals("Publico"))  cadenaProyecto=cadenaProyecto+"OFICIO\t: "+datos[2]+"\n\n";
            else cadenaProyecto=cadenaProyecto+"RUGRO\t: "+datos[2]+"\n\n";
            cadenaProyecto=cadenaProyecto+"DESCRIPCION\t: "+datos[3]+"\n\n";
            cadenaProyecto=cadenaProyecto+"LONGITUD\t: "+datos[4]+"\n\n";//coor y  longitud
            cadenaProyecto=cadenaProyecto+"LATITUD\t: "+datos[5]+"\n\n";//coor X  longitud
            cadenaProyecto=cadenaProyecto+"PASSWORD\t: "+datos[6]+"\n\n";

            AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(RegistrarseOficio.this);
            builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
            builderEditBiodata.setTitle("DESEA REGISTRARSE CON ESTOS DATOS");
            builderEditBiodata.setMessage(cadenaProyecto);
            builderEditBiodata.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //PARA LA CONEXION AL SERVIDOR
                    BackTaskAgregar agregarUsuariosKachuelo;
                    if(valorsp.equals("Publico"))   agregarUsuariosKachuelo=new BackTaskAgregar("usuarios_kachuelo",datos,RegistrarseOficio.this);
                   else agregarUsuariosKachuelo=new BackTaskAgregar("usuarios_empresa",datos,RegistrarseOficio.this);
                    agregarUsuariosKachuelo.execute();
                }
            });

            builderEditBiodata.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builderEditBiodata.show();
        }
        else{
            dialogo=new DialogKachuelo(RegistrarseOficio.this);
            dialogo.cargarDialogo( "Hay campos sin llenar o falta llenar coor. GPS");
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
