package fi.tuni.tamk.moodo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Settings", "onCreate");
        setContentView(R.layout.settings_layout);
        initializeBackgroundTransition();
    }

    private void initializeBackgroundTransition() {
        View containerView = findViewById(R.id.root_view_settings);

        Drawable bg_0 = getResources().getDrawable(R.drawable.bg_blue);
        Drawable bg_1 = getResources().getDrawable(R.drawable.bg_blueorange);
        Drawable bg_2 = getResources().getDrawable(R.drawable.bg_greenyellow);
        Drawable bg_3 = getResources().getDrawable(R.drawable.bg_green);
        Drawable bg_4 = getResources().getDrawable(R.drawable.bg_orange);
        Drawable bg_5 = getResources().getDrawable(R.drawable.bg_redorange);
        Drawable bg_6 = getResources().getDrawable(R.drawable.bg_lightred);
        Drawable bg_7 = getResources().getDrawable(R.drawable.bg_red);
        Drawable bg_8 = getResources().getDrawable(R.drawable.bg_purple);
        Drawable[] drawables = {bg_0, bg_1, bg_2, bg_3, bg_4, bg_5, bg_6, bg_7, bg_8};

        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(drawables);
        containerView.setBackground(ctd);
        ctd.startTransition(4000, 8000); // 1 second transition, 3 second pause between transitions.
    }
}
