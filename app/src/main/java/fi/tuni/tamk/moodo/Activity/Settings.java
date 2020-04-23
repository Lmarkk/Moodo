package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.LocaleHelper;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        Log.d("Settings", "onCreate");
        if(LocaleHelper.getLanguage(this).equals("en")) {
            RadioButton radioEnglish = findViewById(R.id.radio_english);
            radioEnglish.setChecked(true);
        } else {
            RadioButton radioFinnish  = findViewById(R.id.radio_finnish);
            radioFinnish.setChecked(true);
        }
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_settings));
    }

    public void openAbout(View v) {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    public void changeLanguage(View v) {
        Log.d("Settings", "changeLanguage: ");
        if(v.getId() == R.id.radio_english) {
            LocaleHelper.setLocale(this, "en");
        } else {
            LocaleHelper.setLocale(this, "fi");
        }
        Intent i = new Intent(this, MoodoApp.class);
        startActivity(i);
    }
}
