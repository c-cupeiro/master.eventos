package org.masterupv.carloscupeiro.eventos.main.domain.model;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.compat.BuildConfig;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.masterupv.carloscupeiro.eventos.R;

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
    private FirebaseStorage storage;
    private static StorageReference storageRef;
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static String colorFondo;
    public static Boolean acercaDe;
    public static Boolean adviceSplash;


    @Override
    public void onCreate() {
        super.onCreate();
        EventosAplicacion.context = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        eventosReference = database.getReference(ITEMS_CHILD_NAME);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://eventos-114fc.appspot.com");
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings =
                new FirebaseRemoteConfigSettings
                        .Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        /*long cacheExpiration = 0;
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseRemoteConfig.activateFetched();
                getColorFondo();
                getAcercaDe();
                getAdviceSplash();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                colorFondo = mFirebaseRemoteConfig.getString("color_fondo");
                acercaDe = mFirebaseRemoteConfig.getBoolean("acerca_de");
                adviceSplash = mFirebaseRemoteConfig.getBoolean("advice_splash");
            }
        });*/
    }

    public static Context getAppContext() {
        return EventosAplicacion.context;
    }

    public static DatabaseReference getItemsReference() {
        return eventosReference;
    }

    public static void mostrarDialogo(final Context context, final String mensaje, String evento) {
        Intent intent = new Intent(context, Dialogo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("evento", evento);
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
                        .appendQueryParameter("iddevice", idRegistroTarea)
                        .appendQueryParameter("idapp", ID_PROYECTO);
                String parametros =
                        constructorParametros.build().getEncodedQuery();
                String url = URL_SERVIDOR + "registrar.php";
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
                guardarIdRegistroPreferencias(contexto, idRegistroTarea);
            }
        }
    }

    public static void guardarIdRegistroPreferencias(Context context,
                                                     String idRegistro) {
        final SharedPreferences prefs = context.getSharedPreferences(
                "Eventos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("idRegistro", idRegistro);
        editor.commit();
    }

    public static String dameIdRegistroPreferencias(Context context) {
        final SharedPreferences preferencias =
                context.getSharedPreferences("Eventos", Context.MODE_PRIVATE);
        String idRegistro = preferencias.getString("idRegistro", "");
        return idRegistro;
    }

    public static void guardarIdRegistro(Context context, String idRegistro) {
        registrarDispositivoEnServidorWebTask tarea =
                new registrarDispositivoEnServidorWebTask();
        tarea.contexto = context;
        tarea.idRegistroTarea = idRegistro;
        tarea.execute();
    }

    public static void eliminarIdRegistro(Context context) {
        desregistrarDispositivoEnServidorWebTask tarea =
                new desregistrarDispositivoEnServidorWebTask();
        tarea.contexto = context;
        tarea.idRegistroTarea = dameIdRegistroPreferencias(context);
        tarea.execute();
    }

    public static class desregistrarDispositivoEnServidorWebTask
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

    public static StorageReference getStorageReference() {
        return storageRef;
    }

    private void getColorFondo() {
        colorFondo = mFirebaseRemoteConfig.getString("color_fondo");
    }

    private void getAcercaDe() {
        acercaDe = mFirebaseRemoteConfig.getBoolean("acerca_de");
    }

    private void getAdviceSplash() {
        adviceSplash = mFirebaseRemoteConfig.getBoolean("advice_splash");
    }
}
