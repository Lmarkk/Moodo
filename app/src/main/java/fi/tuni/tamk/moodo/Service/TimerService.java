package fi.tuni.tamk.moodo.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import fi.tuni.tamk.moodo.R;

import static fi.tuni.tamk.moodo.TimerNotification.CHANNEL_ID;

public class TimerService extends Service {
    public static final String CONTENT_TITLE = "Moodo App";
    public static final int SERVICE_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start a new thread if heavy operations needed here
        //Intent notificationIntent = new Intent(this, RoutineView.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this,
        //        0, notificationIntent, 0);

        Notification notification  = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(CONTENT_TITLE)
                .setContentText("Moodo is running...")
                .setSmallIcon(R.drawable.ic_alarm)
                //.setContentIntent(pendingIntent)
                .build();

        startForeground(SERVICE_ID, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
