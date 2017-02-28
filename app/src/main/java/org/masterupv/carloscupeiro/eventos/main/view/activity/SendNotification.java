package org.masterupv.carloscupeiro.eventos.main.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.masterupv.carloscupeiro.eventos.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;

import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.ID_PROYECTO;
import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.URL_SERVIDOR;

/**
 * Created by carlos.cupeiro on 28/02/2017.
 */

public class SendNotification extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyDHeAQD5AoAtdun1V4xvpvboF44wgMs9DQ";


    private EditText et_message;
    private Button btn_send;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification_layout);
        activity = this;
        et_message = (EditText) findViewById(R.id.et_message_notification);
        btn_send = (Button) findViewById(R.id.btn_send_notification);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendNotificationTask().execute();
            }
        });
    }

    private class SendNotificationTask
            extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog dialogo;
        private String mensaje;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mensaje = et_message.getText().toString();
            dialogo = new ProgressDialog(activity);
            dialogo.setMessage("Enviando Notificaciones");
            dialogo.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                Uri.Builder constructorParametros = new Uri.Builder()
                        .appendQueryParameter("idapp", ID_PROYECTO)
                        .appendQueryParameter("apiKey", API_KEY)
                        .appendQueryParameter("mensaje", mensaje);
                String parametros = constructorParametros.build().getEncodedQuery();
                String url = URL_SERVIDOR + "notificar.php";
                URL direccion = new URL(url);
                HttpURLConnection conexion = (HttpURLConnection) direccion.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setRequestProperty("Accept-Language", "UTF-8");
                conexion.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conexion.getOutputStream());
                outputStreamWriter.write(parametros.toString());
                outputStreamWriter.flush();
                int respuesta = conexion.getResponseCode();
                if (respuesta==200){
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean resul) {
            dialogo.dismiss();
            String salida ="";
            if(resul){
                salida = "Notificaciones enviadas";
            }else{
                salida = "Fallo al enviar las notificaciones";
            }
            Toast.makeText(activity, mensaje, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

}
