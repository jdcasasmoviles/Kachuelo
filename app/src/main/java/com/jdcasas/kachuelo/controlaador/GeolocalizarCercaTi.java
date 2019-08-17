package com.jdcasas.kachuelo.controlaador;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.jdcasas.kachuelo.MapsActivity;
import com.jdcasas.kachuelo.modelo.BaseDatos;
import com.jdcasas.kachuelo.vista.DialogKachuelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
public class GeolocalizarCercaTi extends AsyncTask< Void, Integer, Void > {
    String cadenacoordenadas="";
    String  hcentro="",kcentro="",valorsp="",tabla="",campo="",error="",mensajeError="";
    String s1="",s2="",s3="",s4="";
    Context context;
    Intent i;
    DialogKachuelo dialogo;
    public GeolocalizarCercaTi(Context context, String tabla, String campo, String hcentro, String kcentro, String valorsp) {
        this.context=context;
        this.hcentro=hcentro;
        this.kcentro=kcentro;
        this.valorsp=valorsp;
        this.tabla=tabla;
        this.campo=campo;
        dialogo=new DialogKachuelo( context );
    }

    @Override
    protected Void doInBackground(Void... params) {
        BaseDatos civiltopia = new BaseDatos();
        String link ="";
        link = civiltopia.buscarCercaTiEmpleados(tabla,campo,hcentro, kcentro,valorsp);
        System.out.println("url..doInBackground\n  :  " + link);
        try {
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result = convertStreamToString(instream);
                    Log.d("result ****", String.valueOf((result)));
                    instream.close();
                    try{
                        JSONArray jArray =new JSONArray(result);
                        llenar(jArray);
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        error="e3";
                        mensajeError="NO HAY RESULTADOS\nJSON";//NO HAY TABLA CREADA TAL VEZ
                    }
                    return null;
                }
            } else { error="e2";
                mensajeError="NO HAY RESPUESTA DEL SERVIDOR";//EL SERVIDOR SUFRO FALLOS
            }

        } catch (Exception e1) {
            error="e1";
            mensajeError="NO HAY RESULTADOS\nSERVIDOR";//NO HAY RESPUESTA.TAL VEZ LA CONSULTA TIENE RESULTADOS CERO
        }
        return null;
    }

    protected void onPostExecute(Void result){
        if(error.equals("e1") || error.equals("e2") || error.equals("e3") )
            dialogo.cargarDialogo(mensajeError);
        else{i=new Intent(context, MapsActivity.class);
            i.putExtra("campo1",getCadenacoordenadas());//////coordendas de los otros
            i.putExtra("campo2", hcentro);/////TU COOR X
            i.putExtra("campo3", kcentro );//////TU COOR Y
            i.putExtra("campo4", tabla );//////tabla
            context.startActivity(i);
        }
    }

    protected void onCancelled() {
        Toast.makeText(context, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
    }

    public void llenar( JSONArray arrayBD) throws JSONException {
        for (int i = 0; i < arrayBD.length(); i++) {
            JSONObject jsonChildNode = arrayBD.getJSONObject(i);
            if(jsonChildNode.optString("resultado").equals("vacio")){setCadenacoordenadas(jsonChildNode.optString("resultado"));break;}
            s1 = jsonChildNode.optString("coorx");
            s2 = jsonChildNode.optString("coory");
            s3 = jsonChildNode.optString("nombre");
            s4 = jsonChildNode.optString("usuario");///celular
            setCadenacoordenadas( getCadenacoordenadas() +s1+"*"+s2+"*"+s3+"*"+s4+"*" );
            System.out.println("cadenasss : " + s1 + "<>" + s2 + "<>"+s3+ "<>"+s4);
            if(isCancelled()) {break;}
        }
        if(getCadenacoordenadas().equals("vacio")){ dialogo.cargarDialogo("NO HAY DATOS"); }
    }

    private String convertStreamToString(InputStream in) {
        int BUFFER_SIZE = 2000;
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            // Handle Exception
            e.printStackTrace();
            return "";
        }
        return str;
    }

    public String getCadenacoordenadas() {
        return cadenacoordenadas;
    }
    public void setCadenacoordenadas(String cadenacoordenadas) {
        this.cadenacoordenadas = cadenacoordenadas;
    }
}
