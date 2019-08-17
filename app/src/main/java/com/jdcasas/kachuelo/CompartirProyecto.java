package com.jdcasas.kachuelo;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.text.DateFormat;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.jdcasas.kachuelo.controlaador.SpinnerLoad;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

public class CompartirProyecto extends AppCompatActivity {
    private StorageReference mStorageRef;
    ImageView item_image1,item_image2;
    Button ButtonCompartir,ButtonVolver;
    EditText et_nombreproyecto,et_descripcion,et_creado_por;
    private static int GALERY_INTENT=1;
    private static int PICK_IMAGE_REQUEST=1;
    public DialogKachuelo dialogo;
    String id_lugar,id_usuario,nombreproyecto,descripcion,creadoPor,oficio;
    Uri[] filepath =new  Uri[2];
    String[] imagenes=new String[2] ;
    int bandera1=0,bandera2=0,i;
    Intent intent;
    Bundle extras;
    BaseDatos civiltopia=new BaseDatos(  );
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_compartir_proyecto );
        recibeDatosActividad();
        dialogo=new DialogKachuelo( CompartirProyecto.this );
        ButtonVolver = (Button) findViewById(R.id.ButtonVolver);
        ButtonCompartir = (Button) findViewById(R.id.ButtonCompartir);
        item_image1 = (ImageView) findViewById(R.id.item_image1);
        item_image2 = (ImageView) findViewById(R.id.item_image2);
        et_creado_por = (EditText) findViewById(R.id.et_creado_por);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_nombreproyecto = (EditText) findViewById(R.id.et_nombreproyecto);
        et_creado_por.setText(creadoPor);
        imagenes[0]="";
        imagenes[1]="";
        DateFormat fecha=new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        id_lugar=id_usuario+"_"+fecha.format(new java.util.Date());//id_lugar
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ButtonCompartir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nombreproyecto=et_nombreproyecto.getText().toString().trim();//nombre del proyecto
                descripcion=et_descripcion.getText().toString().trim();//descripcion
                if(TextUtils.isEmpty(descripcion)  )dialogo.cargarDialogo( "COLOCAR DESCRIPCION" );
                else if(TextUtils.isEmpty(nombreproyecto)  )dialogo.cargarDialogo( "COLOCAR NOMBRE DEL PROYECTO" );
                else if(imagenes[0].equals( "" )  ||  imagenes[1].equals( "" )){
                    dialogo.cargarDialogo( "ELIJA UNA IMAGEN " );
                }
                else {
                    i=0;
                    loading = ProgressDialog.show(CompartirProyecto.this, "Please Wait",null, true, true);
                    subirArchivos();

                }
            }
        });
        ButtonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent( CompartirProyecto.this, MainActivity.class );
                intent.putExtra( "campo1",creadoPor );//nombre
                intent.putExtra( "campo2",id_usuario);//cell
                intent.putExtra("campo3", oficio);//oficio
                startActivity(intent);
                finish();
            }
        });
        item_image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult( intent,GALERY_INTENT );
                bandera1=1;
            }
        });
        item_image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult( intent,GALERY_INTENT );
                bandera2=1;
            }
        });
    }

    public void recibeDatosActividad(){
        extras = getIntent().getExtras();
        if (extras != null) {
            creadoPor  = extras.getString("campo1");//nombre
            id_usuario  = extras.getString("campo2");//celular
            oficio  = extras.getString("campo3");//oficio
        }else finish();
    }
    ////////////este metodo es recursivo finaliza cuendo todas las imagenes se hallan subido al storage
    public void  subirArchivos(){
        if(filepath!=null && imagenes!=null  && i< filepath.length) {
            imagenes[i] = "fotos/" + id_lugar + "/" + filepath[i].getLastPathSegment();//imagen 1
            StorageReference filePathFirebase = mStorageRef.child( "fotos" ).child( id_lugar ).child( filepath[i].getLastPathSegment() );
            filePathFirebase.putFile( filepath[i])
                    .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            i++;
                            subirArchivos();
                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            dialogo.cargarDialogo( "ERROR  : " + exception.toString() );
                        }
                    } );
        }
        else if(i==2) {
            loading.dismiss();
            final String[] datos = new String[5];
            datos[0]=nombreproyecto;
            datos[1]=descripcion;
            datos[2]=creadoPor;
            datos[3]=imagenes[0];
            datos[4]=imagenes[1];
            SpinnerLoad cargarInsertar=new SpinnerLoad(CompartirProyecto.this);
            cargarInsertar.loadInsertarProyectos( civiltopia.insertarProyectos("proyectos", datos) );
        }
        else dialogo.cargarDialogo( "ELIJA IMAGENES" );
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult( requestCode,resultCode,data );
        if(requestCode==GALERY_INTENT && requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            try {
                if( bandera1==1){
                    filepath[0]=data.getData();
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap( getContentResolver(),filepath[0] );
                    bitmap=Bitmap.createScaledBitmap(bitmap, 180,180, true);
                    item_image1.setImageBitmap( bitmap );
                    bandera1=0;
                    imagenes[0]=filepath[0].getLastPathSegment();
                }
                else if( bandera2==1){
                    filepath[1]=data.getData();
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap( getContentResolver(),filepath[1] );
                    bitmap=Bitmap.createScaledBitmap(bitmap, 180,180, true);
                    item_image2.setImageBitmap( bitmap );
                    bandera2=0;
                    imagenes[1]=filepath[1].getLastPathSegment();
                }
            } catch (IOException e) {
                e.printStackTrace();
                dialogo.cargarDialogo( "ERROR " +e.toString());
            }
        }
        else dialogo.cargarDialogo( "NO SE CARGO LA IMAGEN" );
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
