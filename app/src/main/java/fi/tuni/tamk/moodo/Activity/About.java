package fi.tuni.tamk.moodo.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

public class About extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        Util.initializeBackgroundTransition(findViewById(R.id.about_root));
    }
}
