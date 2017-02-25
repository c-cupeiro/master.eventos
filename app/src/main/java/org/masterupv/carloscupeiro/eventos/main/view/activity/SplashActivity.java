package org.masterupv.carloscupeiro.eventos.main.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.masterupv.carloscupeiro.eventos.BuildConfig;
import org.masterupv.carloscupeiro.eventos.R;

import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.acercaDe;
import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.adviceSplash;
import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.colorFondo;
import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.mFirebaseRemoteConfig;

/**
 * Created by Carlos on 25/02/2017.
 */

public class SplashActivity extends AppCompatActivity {
    public static final long TIME = 3000;
    private Thread welcomeThread;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(TIME);
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.toString());
                } finally {
                    startActivity(new Intent(SplashActivity.this,ActividadPrincipal.class));
                    finish();
                }
            }
        };
    }

    protected void onPostResume() {
        super.onPostResume();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        long cacheExpiration = 0;
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFirebaseRemoteConfig.activateFetched();
                        colorFondo = mFirebaseRemoteConfig.getString("color_fondo");
                        acercaDe = mFirebaseRemoteConfig.getBoolean("acerca_de");
                        adviceSplash = mFirebaseRemoteConfig.getBoolean("advice_splash");
                        if(adviceSplash){
                            TextView advice = (TextView) findViewById(R.id.tv_advertencia);
                            advice.setVisibility(View.VISIBLE);
                        }
                        welcomeThread.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        colorFondo = mFirebaseRemoteConfig.getString("color_fondo");
                        acercaDe = mFirebaseRemoteConfig.getBoolean("acerca_de");
                        adviceSplash = mFirebaseRemoteConfig.getBoolean("advice_splash");
                        if(adviceSplash){
                            TextView advice = (TextView) findViewById(R.id.tv_advertencia);
                            advice.setVisibility(View.VISIBLE);
                        }
                        welcomeThread.start();
                    }
                });
    }

}
