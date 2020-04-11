package fi.tuni.tamk.moodo.Classes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "timerServiceChannel";
    public static final String CHANNEL_NAME = "timerServiceChannel";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService((NotificationManager.class));
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static Context getContext(){
        return mContext;
    }
}
