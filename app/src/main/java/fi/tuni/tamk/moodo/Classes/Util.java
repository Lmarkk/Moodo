package fi.tuni.tamk.moodo.Classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import fi.tuni.tamk.moodo.R;

public class Util {

    public static void initializeBackgroundTransition(Context context, View view) {
        Drawable bg_0 = context.getDrawable(R.drawable.bg_blue);
        Drawable bg_1 = context.getDrawable(R.drawable.bg_blueorange);
        Drawable bg_2 = context.getDrawable(R.drawable.bg_greenyellow);
        Drawable bg_3 = context.getDrawable(R.drawable.bg_green);
        Drawable bg_4 = context.getDrawable(R.drawable.bg_orange);
        Drawable bg_5 = context.getDrawable(R.drawable.bg_redorange);
        Drawable bg_6 = context.getDrawable(R.drawable.bg_lightred);
        Drawable bg_7 = context.getDrawable(R.drawable.bg_red);
        Drawable bg_8 = context.getDrawable(R.drawable.bg_purple);
        Drawable[] drawables = {bg_0, bg_1, bg_2, bg_3, bg_4, bg_5, bg_6, bg_7, bg_8};

        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(drawables);
        view.setBackground(ctd);
        ctd.startTransition(4000, 8000); // 1 second transition, 3 second pause between transitions.
    }
}