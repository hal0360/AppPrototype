package cpit.prototype;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SchedulePop extends BroadcastReceiver {
    public SchedulePop() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PopService.class);
        context.startService(i);
    }

    public void starting(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent("PhilAppPopup");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000 * pref.getInt("popspan", 5), pi);
    }

    public void cancelling(Context context)
    {
        Intent intent = new Intent("PhilAppPopup");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
