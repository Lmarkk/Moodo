package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.LocaleHelper;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

import android.util.Log;
import android.view.View;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Settings", "onCreate");
        setContentView(R.layout.settings_layout);
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_settings));
    }

    public void openAbout(View v) {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    public void changeLanguage(View v) {
        Log.d("Settings", "changeLanguage: ");
        if(v.getId() == R.id.btn_language_eng) {
            LocaleHelper.setLocale(this, "en");
        } else {
            LocaleHelper.setLocale(this, "fi");
        }
        Intent i = new Intent(this, MoodoApp.class);
        startActivity(i);
    }
}
