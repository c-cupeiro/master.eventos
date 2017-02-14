package org.masterupv.carloscupeiro.eventos.main.domain.model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by carlos.cupeiro on 13/02/2017.
 */

public class EventosAplicacion extends Application {
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String URL_SERVIDOR =
            "http://cursoandroid.hol.es/notificaciones/";
    public static String ID_PROYECTO = "eventos-114fc";
    public String idRegistro = "";
    private String ITEMS_CHILD_NAME = "eventos";
    private static DatabaseReference eventosReference;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        EventosAplicacion.context = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        eventosReference = database.getReference(ITEMS_CHILD_NAME);
    }

    public static Context getAppContext() {
        return EventosAplicacion.context;
    }

    public static DatabaseReference getItemsReference() {
        return eventosReference;
    }

    public static void mostrarDialogo(final Context context, final String mensaje,String evento) {
        Intent intent = new Intent(context, Dialogo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mensaje" , mensaje);
        intent.putExtra("evento" , evento);
        context.startActivity(intent);
    }

    public static class registrarDispositivoEnServidorWebTask
            extends AsyncTask<Void, Void, String> {
        String response = "error";
        Context contexto;
        String idRegistroTarea = "";

        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                Uri.Builder constructorParametros = new Uri.Builder()
                        .appendQueryParameter("iddevice" , idRegistroTarea)
                        .appendQueryParameter("idapp" , ID_PROYECTO);
                String parametros =
                        constructorParametros.build().getEncodedQuery();
                String url = URL_SERVIDOR + "registrar.php";
                URL direccion = new URL(url);
                HttpURLConnection conexion = (HttpURLConnection)
                        direccion.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("Accept-Language" , "UTF-8");
                conexion.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new
                        OutputStreamWriter(conexion.getOutputStream());
                outputStreamWriter.write(parametros.toString());
                outputStreamWriter.flush();
                int respuesta = conexion.getResponseCode();
                if (respuesta == 200) {
                    response = "ok";
                } else {
                    response = "error";
                }
            } catch (IOException e) {
                response = "error";
            }
            return response;
        }

        public void onPostExecute(String res) {
            if (res == "ok") {
                guardarIdRegistroPreferencias(contexto, idRegistroTarea);
            }
        }
    }

    public static void guardarIdRegistroPreferencias(Context context,
                                                     String idRegistro) {
        final SharedPreferences prefs = context.getSharedPreferences(
                "Eventos" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("idRegistro" , idRegistro);
        editor.commit();
    }

    public static String dameIdRegistroPreferencias(Context context) {
        final SharedPreferences preferencias =
                context.getSharedPreferences("Eventos" , Context.MODE_PRIVATE);
        String idRegistro = preferencias.getString("idRegistro" , "");
        return idRegistro;
    }

    public static void guardarIdRegistro(Context context, String idRegistro) {
        registrarDispositivoEnServidorWebTask tarea =
                new registrarDispositivoEnServidorWebTask();
        tarea.contexto = context;
        tarea.idRegistroTarea = idRegistro;
        tarea.execute();
    }
    public static void eliminarIdRegistro(Context context){
        desregistrarDispositivoEnServidorWebTask tarea =
                new desregistrarDispositivoEnServidorWebTask();
        tarea.contexto=context;
        tarea.idRegistroTarea="";
        tarea.execute();
    }
    public static class desregistrarDispositivoEnServidorWebTask
            extends AsyncTask<Void, Void, String> {
        String response="error";
        Context contexto;
        String idRegistroTarea ="";
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            try {
                Uri.Builder constructorParametros = new Uri.Builder()
                        .appendQueryParameter("iddevice", idRegistroTarea)
                        .appendQueryParameter("idapp", ID_PROYECTO);
                String parametros =
                        constructorParametros.build().getEncodedQuery();
                String url = URL_SERVIDOR + "desregistrar.php";
                URL direccion = new URL(url);
                HttpURLConnection conexion = (HttpURLConnection)
                        direccion.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("Accept-Language", "UTF-8");
                conexion.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new
                        OutputStreamWriter(conexion.getOutputStream());
                outputStreamWriter.write(parametros.toString());
                outputStreamWriter.flush();
                int respuesta = conexion.getResponseCode();
                if (respuesta == 200) {
                    response = "ok";
                } else {
                    response = "error";
                }
            } catch (IOException e) {
                response = "error";
            }
            return response;
        }
        public void onPostExecute(String res) {
            if (res == "ok") {
                guardarIdRegistroPreferencias(contexto, "");
            }
        }
    }
}
