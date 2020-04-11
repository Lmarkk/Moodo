package fi.tuni.tamk.moodo.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

import android.util.Log;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Settings", "onCreate");
        setContentView(R.layout.settings_layout);
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_settings));
    }
}
