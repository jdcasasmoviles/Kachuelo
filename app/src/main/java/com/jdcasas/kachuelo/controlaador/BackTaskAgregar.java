package com.jdcasas.kachuelo.controlaador;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
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

public class BackTaskAgregar extends AsyncTask<Void, Integer, Void> {
    String error="",mensajeError="",tabla="",cadenaResultado="";
    String[] datos = new String[9];
    Context context;
    DialogKachuelo dialogo;
    public BackTaskAgregar(String tabla,String[] datos,Context context) {
        this.tabla=tabla;
        this.datos=datos;
        this.context=context;
        dialogo=new DialogKachuelo( context );
    }

    @Override
    protected Void doInBackground(Void... params) {
        BaseDatos civiltopia = new BaseDatos();
        String link ="";
        if(tabla.equals( "usuarios_kachuelo" ) || tabla.equals( "usuarios_empresa" )  )link=civiltopia.insertarUsuariosKachuelo(tabla, datos);
        else link=civiltopia.insertarPublicarEmpleo(tabla, datos);
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
            //DATO NO INSERTA YA QUE ES REPETIDO
        }
        return null;
    }

    protected void onPostExecute(Void result){
        if(error.equals("e1") || error.equals("e2") || error.equals("e3") )
            dialogo.cargarDialogo(mensajeError);
        else{
            if(tabla.equals( "usuarios_kachuelo" ) || tabla.equals( "usuarios_empresa" )  )dialogo.cargarDialogo( cadenaResultado );
            else dialogo.cargarDialogo( "Gracias por publicar tu aviso.\nKachuelo" );
        }
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
    }

    public void llenar( JSONArray arrayBD) throws JSONException {
        for (int i = 0; i < arrayBD.length(); i++) {
            JSONObject jsonChildNode = arrayBD.getJSONObject(i);
            if(jsonChildNode.optString("resultado").equals("vacio")){cadenaResultado=jsonChildNode.optString("resultado");break;}
            cadenaResultado =cadenaResultado+"Con estos valores se podra loguear,recordar estos valores .\nGracias.\n\n";
            cadenaResultado =cadenaResultado+"USUARIO : "+ jsonChildNode.optString("usuario")+"\n\n";
            cadenaResultado =cadenaResultado+"PASSWORD : "+ jsonChildNode.optString("password")+"\n";
            if(isCancelled()) {break;}
        }
        if(cadenaResultado.equals("vacio")){ dialogo.cargarDialogo("NO HAY DATOS"); }
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
}
