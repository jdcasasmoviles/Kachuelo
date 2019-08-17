package com.jdcasas.kachuelo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Marker;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String Respuesta = "", fila = "", fila2 = "",tabla="";
    private CameraUpdate mCamera;
    double coorcamaraX, coorcamaraY, coorX, coorY;
    Bundle extras;
    DialogKachuelo dialogo=new DialogKachuelo( MapsActivity.this );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
    }

    public void onStart() {
        super.onStart();
        //Obtenemos datos enviados
        extras = getIntent().getExtras();
        if (extras != null) {
            Respuesta = extras.getString( "campo1" );
            coorcamaraX = Double.parseDouble( extras.getString( "campo2" ) );
            coorcamaraY = Double.parseDouble( extras.getString( "campo3" ) );
            tabla = extras.getString( "campo4" );
        }
        //   coorcamaraX =-13.085258;/////////Latitud/////////onCrete  va las coordenas que se dibuja en map
        //  coorcamaraY =-76.387868;///////////longitud////////solo aqui nada mas y se inicia normalmente
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMapIfNeeded();
        setMarker( new LatLng( coorcamaraX, coorcamaraY ), "TU",
                "Posicion actual", 0.3F, 0.5F, 0.5F, R.drawable.yo );
        while (!Respuesta.equals( "" )) {
            //toma la cadena 0 hasta un *
            fila = Respuesta.substring( 0, Respuesta.indexOf( '*' ) );
            coorX = Double.parseDouble( fila );
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring( Respuesta.indexOf( '*' ) + 1, Respuesta.length() );

            //toma la cadena 0 hasta un *
            fila = Respuesta.substring( 0, Respuesta.indexOf( '*' ) );
            coorY = Double.parseDouble( fila );
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring( Respuesta.indexOf( '*' ) + 1, Respuesta.length() );

            fila = Respuesta.substring( 0, Respuesta.indexOf( '*' ) );
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring( Respuesta.indexOf( '*' ) + 1, Respuesta.length() );

            //toma la cadena 0 hasta un *
            fila2 = Respuesta.substring( 0, Respuesta.indexOf( '*' ) );
            //mocha la cadena desde * en adelante hasta el final
            Respuesta = Respuesta.substring( Respuesta.indexOf( '*' ) + 1, Respuesta.length() );
            System.out.println( "dentro del while" + coorX + " " + coorY + " " + fila + " " + fila2 );
            ;
            setUpMapIfNeeded();
          if(tabla.equals( "usuarios_kachuelo" ))  setMarker( new LatLng( coorX, coorY ), fila,
                    "Contacto : " + fila2, 0.3F, 0.5F, 0.5F, R.drawable.empleado );
          else if(tabla.equals( "usuarios_empresa" ))  setMarker( new LatLng( coorX, coorY ), fila,
                    "Contacto : " + fila2, 0.3F, 0.5F, 0.5F, R.drawable.empresa );
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled( true );
            mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
            setUpMap();
        }
    }

    private void setUpMap() {
    /*    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getSnippet().equals("")){
                    String strPhone = "995949259";
                    String strMessage = "Kachuelo : El cliente con el numero 123 requiere tus servicios comunicate con el";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.putExtra("address", strPhone);
                    sendIntent.putExtra("sms_body", strMessage);
                    startActivity(sendIntent);
                     SmsManager sms = SmsManager.getDefault();
                    ArrayList messageParts = sms.divideMessage(strMessage);
                    sms.sendMultipartTextMessage(strPhone, null, messageParts, null, null);
                   System.out.println("ENVIARRRRRRRRRRRRRRRR ");
                    return false;
                }
                else{
                    return false;
                }
            }
        });*/

        mCamera = CameraUpdateFactory.newLatLngZoom( new LatLng(
                coorcamaraX, coorcamaraY ), 14 );
        mMap.animateCamera( mCamera );
    }

    private void setMarker(LatLng position, String title, String info,
                           float opacity, float dimension1, float dimension2, int icon) {
        mMap.addMarker( new MarkerOptions()
                .position( position )
                .title( title )
                .snippet( info )
                .alpha( opacity )
                .anchor( dimension1, dimension2 )
                .icon( BitmapDescriptorFactory.fromResource( icon ) ) );
    }

    /////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuOpcion1:
                mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
                return true;
            case R.id.MenuOpcion2:
                mMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE );
                return true;
            case R.id.MenuOpcion3:
                mMap.setMapType( GoogleMap.MAP_TYPE_TERRAIN );
                return true;
            case R.id.MenuOpcion4:
                mMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main_mapa, menu );
        return true;
    }
}
