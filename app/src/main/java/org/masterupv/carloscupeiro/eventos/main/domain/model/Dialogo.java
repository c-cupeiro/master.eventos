package org.masterupv.carloscupeiro.eventos.main.domain.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import org.masterupv.carloscupeiro.eventos.main.view.activity.EventoDetalles;

/**
 * Created by carlos.cupeiro on 13/02/2017.
 */

public class Dialogo extends Activity {@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Intent intent = getIntent();
    final Bundle extras = intent.getExtras();
    if (intent.hasExtra("mensaje")) {
        final Context context = this;
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Mensaje:");
        alertDialog.setMessage(extras.getString("mensaje"));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CERRAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (intent.hasExtra("evento") && !extras.getString("evento").equals("")) {
                            Intent intent_evento = new Intent(context, EventoDetalles.class);
                            intent_evento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent_evento.putExtra("evento", extras.getString("evento"));
                            context.startActivity(intent_evento);
                        }
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
        extras.remove("mensaje");
    }
}

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
