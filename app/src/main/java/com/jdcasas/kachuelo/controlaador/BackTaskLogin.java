package com.jdcasas.kachuelo.controlaador;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.jdcasas.kachuelo.MainActivity;
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
public class BackTaskLogin extends AsyncTask< String, Integer, String > {
    String tabla="",usuario = "",password = "",error="",mensajeError="",respuestaServidor = "";
    Context context;
    DialogKachuelo dialogo;
    String s1="",s2="",s3="",s4="";
    Intent i;
    public BackTaskLogin(Context context,String tabla,String usuario, String password) {
        this.context=context;
        this.tabla=tabla;
        this.usuario = usuario;
        this.password = password;
        dialogo=new DialogKachuelo( context );
    }

    protected String doInBackground(String... params) {
        String link="";
        BaseDatos civiltopia = new BaseDatos(1);
        link = civiltopia.accesoLoginKachuelo(tabla,usuario, password);
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
                    respuestaServidor=result ;
                    instream.close();
                }
            } else { error="e2";
                mensajeError="NO HAY RESPUESTA DEL SERVIDOR";
            }
            return respuestaServidor;
        } catch (Exception e1) {
            error="e1";
            mensajeError="NO HAY CONEXION AL SERVIDOR";
            return new String("Exception: " + e1.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        if(error.equals("e1") || error.equals("e2") ) dialogo.cargarDialogo(mensajeError);
        else {
            try {
                JSONArray arrayBD = new JSONArray(result);
                if (arrayBD!=null && arrayBD.length() > 0) {
                    for (int i = 0; i < arrayBD.length(); i++) {
                        JSONObject jsonChildNode = arrayBD.getJSONObject(i);
                        if (jsonChildNode.optString("logstatus").equals("0")) {
                            error = "e3";
                            mensajeError = "Nombre de usuario o password incorrectos";
                            break;
                        }
                        s1= jsonChildNode.optString("nombre");
                        s2= jsonChildNode.optString("celular") ;
                       s3= jsonChildNode.optString("oficio");
                        s4= jsonChildNode.optString("descripcion");
                    }
                    if(error.equals("e3") ) dialogo.cargarDialogo(mensajeError);
                    else {
                         i=new Intent(context, MainActivity.class);
                        i.putExtra("campo1", s1);//nombre
                        i.putExtra("campo2", s2);//celular
                        i.putExtra("campo3", s3);//oficio
                        context.startActivity(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        System.out.println("avance " + progreso);
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onCancelled() {
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
            e.printStackTrace();
            return "";
        }
        return str;
    }
}
