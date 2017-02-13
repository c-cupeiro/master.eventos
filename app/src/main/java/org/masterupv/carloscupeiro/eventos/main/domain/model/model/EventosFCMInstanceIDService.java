package org.masterupv.carloscupeiro.eventos.main.domain.model.model;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static org.masterupv.carloscupeiro.eventos.main.domain.model.EventosAplicacion.guardarIdRegistro;

/**
 * Created by carlos.cupeiro on 13/02/2017.
 */

public class EventosFCMInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String idPush;
        idPush = FirebaseInstanceId.getInstance().getToken();
        guardarIdRegistro(getApplicationContext(), idPush);
    }

}
