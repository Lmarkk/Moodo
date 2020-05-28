package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(() -> {
            Intent i=new Intent(SplashScreen.this, MoodoApp.class);

            startActivity(i);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
