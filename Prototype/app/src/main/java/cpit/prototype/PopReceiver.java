package cpit.prototype;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PopReceiver extends BroadcastReceiver {
    public PopReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref;
        pref = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        if(pref.getBoolean("logged", false)) {

            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    new Intent("philAppStamp"),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (!alarmUp)
            {
                UpdateReceiver ta = new UpdateReceiver();
                ta.starting(context);
            }


            Intent i = new Intent(context, PopService.class);
            context.startService(i);
        }
    }
}
