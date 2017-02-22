package org.masterupv.carloscupeiro.eventos.main.domain.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.masterupv.carloscupeiro.eventos.main.domain.model.EventosFCMService;

/**
 * Created by Carlos on 22/02/2017.
 */

public class ReceptorInicio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, EventosFCMService.class));
    }
}
