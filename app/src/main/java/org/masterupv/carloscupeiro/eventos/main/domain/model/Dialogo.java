package org.masterupv.carloscupeiro.eventos.main.domain.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by carlos.cupeiro on 13/02/2017.
 */

public class Dialogo extends Activity {@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle extras = getIntent().getExtras();
    if (getIntent().hasExtra("mensaje")) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Mensaje:");
        alertDialog.setMessage(extras.getString("mensaje"));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CERRAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
        extras.remove("mensaje");
    }
}

}
