package com.jdcasas.kachuelo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jdcasas.kachuelo.controlaador.BackTaskAgregar;
import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import java.util.ArrayList;
public class PublicarEmpleo extends AppCompatActivity {
    ArrayList<String> listOficio= new ArrayList<>();
    ArrayList<String> listProvincia= new ArrayList<>();
    ArrayList<String> listDistrito= new ArrayList<>();
    ArrayAdapter<String> adapterOficio,adapterProvincia,adapterDistrito;
    Spinner sp_oficio,sp_provincia,sp_distrito;
    EditText et_descripcion,et_contacto,et_sueldo;
    Button bt_publicar,ButtonReset,ButtonVolver;
    String departamento="";
    DialogKachuelo dialogo;
    BaseDatos civiltopia = new BaseDatos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_publicar_empleo );
        dialogo=new DialogKachuelo( PublicarEmpleo.this );
        //PROCESO PARA OBTENER EL NOMBRE DE DEPARTAMENTO
        Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) { departamento  = extras.getString("valorsp"); }
        else  departamento= "error";
        //TEXTVIEW DEPARTAMENTO
        TextView tw_registrarse = (TextView)findViewById(R.id.tw_registrarse);
        tw_registrarse.setText("Publicar  Empleo " + departamento.toUpperCase());
        //EDITTEXT
        this.et_sueldo = (EditText) findViewById(R.id.et_sueldo);
        this.et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        this.et_contacto = (EditText) findViewById(R.id.et_contacto);
        //BOTONES
        this.bt_publicar = (Button) findViewById(R.id.bt_publicar);
        this.ButtonReset = (Button) findViewById(R.id.ButtonReset);
        this.ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
       //SPINNER  OFICIO
        this.sp_oficio = (Spinner) findViewById(R.id.sp_oficio);
        adapterOficio = new ArrayAdapter<String>(this, R.layout.spinner_oficio_layout, R.id.txtspoficio, listOficio);
        sp_oficio.setAdapter(adapterOficio);
        sp_oficio.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
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
                       // BackTaskSpinner cargarDistritos=new BackTaskSpinner(listDistrito,adapterDistrito,"spinner_distrito","distrito",PublicarEmpleo.this,departamento,provincia);
                     //   cargarDistritos.execute();
                        SpinnerLoad cargarDistritos=new SpinnerLoad(listDistrito,adapterDistrito,PublicarEmpleo.this );
                        cargarDistritos.loadSpinner( civiltopia.listaDepasDistritos( departamento,provincia ) );
                        sp_distrito.setAdapter(adapterDistrito);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //BOTON PARA PUBLICAR
        bt_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] datos = new String[8];
                datos[0]= sp_provincia.getItemAtPosition(sp_provincia.getSelectedItemPosition()).toString();
                datos[1]=sp_distrito.getItemAtPosition(sp_distrito.getSelectedItemPosition()).toString();
                datos[2]= sp_oficio.getItemAtPosition(sp_oficio.getSelectedItemPosition()).toString();
                datos[3]= et_sueldo.getText().toString();
                datos[4]= et_contacto.getText().toString();
                datos[5]=et_descripcion.getText().toString();
                ///fecha
                 java.text.DateFormat fecha=new java.text.SimpleDateFormat("dd/MM/yyyy");
                   datos[6]=fecha.format(new java.util.Date());
                datos[7]=departamento;
                dialogoRegistrar(datos);

            }
        });
         //BOTON RESET
        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_contacto.setText("");
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
    }

    public void onStart(){
        super.onStart();
        SpinnerLoad cargarOficios=new SpinnerLoad( listOficio,adapterOficio,PublicarEmpleo.this );
        cargarOficios.loadSpinner(civiltopia.spOpciones( "spinner_oficio","oficio" ));
        SpinnerLoad  cargarProvincias=new SpinnerLoad(listProvincia,adapterProvincia,PublicarEmpleo.this);
        cargarProvincias.loadSpinner(civiltopia.listarDepasProvincias(departamento));
    }
    //////////////////////////METODO PARA REGISTRAR////////////////////////////////////////////////////
    public void dialogoRegistrar(final String[] datos){
        if(!datos[0].equals("")&& !datos[1].equals("")  && !datos[2].equals("") && !datos[3].equals("") && !datos[4].equals("") && !datos[5].equals("")){
            String cadenaProyecto="\n";
            cadenaProyecto=cadenaProyecto+"PROVINCIA\t: "+datos[0]+"\n\n";
            cadenaProyecto=cadenaProyecto+"DISTRITO\t: "+datos[1]+"\n\n";
            cadenaProyecto=cadenaProyecto+"OFICIO\t: "+datos[2]+"\n\n";
            cadenaProyecto=cadenaProyecto+"SUELDO\t: "+datos[3]+"\n\n";
            cadenaProyecto=cadenaProyecto+"CONTACTO\t: "+datos[4]+"\n\n";
            cadenaProyecto=cadenaProyecto+"DESCRIPCION\t: "+datos[5]+"\n\n";
            cadenaProyecto=cadenaProyecto+"FECHA\t: "+datos[6]+"\n\n";
            cadenaProyecto=cadenaProyecto+"DEPARTAMENTO\t: "+datos[7]+"\n\n";
            AlertDialog.Builder builderEditBiodata = new AlertDialog.Builder(PublicarEmpleo.this);
            builderEditBiodata.setIcon(R.drawable.ic_menu_white_24dp);
            builderEditBiodata.setTitle("DESEA PUBLICAR CON ESTOS DATOS");
            builderEditBiodata.setMessage(cadenaProyecto);
            builderEditBiodata.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //PARA LA CONEXION AL SERVIDOR
                  BackTaskAgregar agregarEmpleo=new BackTaskAgregar("lima_publicarempleo",datos,PublicarEmpleo.this);
                    agregarEmpleo.execute();
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
            dialogo.cargarDialogo( "Hay campos sin llenar " );
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
