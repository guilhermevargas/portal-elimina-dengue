package controller.notificador;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import br.com.aedes.R;
import view.activity.AgendaActivity;


/**
 * Created by Alexandre on 22/03/2015.
 */
public class AedesReceiver extends BroadcastReceiver {
    private String ticker;
    private String titulo;
    private String mensagem;
    private int icone;


    public AedesReceiver() {
        this.ticker = "Aedes";
        this.titulo = "Titulo";
        this.mensagem = "Mensagem";
        this.icone = R.drawable.ic_aedes_launcher;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.titulo = intent.getStringExtra("titulo");
        this.mensagem = intent.getStringExtra("mensagem");
        this.icone = intent.getIntExtra("icone", icone);

        gerarNotificacao(context, new Intent(context, AgendaActivity.class));
    }


    public void gerarNotificacao(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(titulo);
        builder.setContentText(mensagem);
        builder.setSmallIcon(R.drawable.icon_aedes);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icone));
        builder.setContentIntent(p);
        

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.icon_aedes, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }
}
