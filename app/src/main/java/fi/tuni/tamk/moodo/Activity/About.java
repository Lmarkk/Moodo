package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
    public void onClickLink(View view) {
        String url = getResources().getString(R.string.link_RoutineCalendar_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
