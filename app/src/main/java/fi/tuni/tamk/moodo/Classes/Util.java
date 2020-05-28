package fi.tuni.tamk.moodo.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import fi.tuni.tamk.moodo.R;

public class Util {
    private static final String FILE_NAME = "custom_routine_data.json";

    /**
     * Distinguishes different kinds of app starts:
     * First start ever (FIRST_TIME)
     * First start in this version (FIRST_TIME_VERSION)
     * Normal app start (NORMAL)
     *
     */
    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }

    private static final String LAST_APP_VERSION = "last_app_version";
    private static final String EXPERIENCE_POINTS ="experience_points";
    private static final String EXPERIENCE_NEEDED_FOR_LVL ="experience_needed";
    private static final String LEVEL ="user_level";

    public static void initializeBackgroundTransition(View view) {
        Drawable bg_0 = App.getContext().getDrawable(R.drawable.bg_blue);
        Drawable bg_1 = App.getContext().getDrawable(R.drawable.bg_blueorange);
        Drawable bg_2 = App.getContext().getDrawable(R.drawable.bg_greenyellow);
        Drawable bg_3 = App.getContext().getDrawable(R.drawable.bg_green);
        Drawable bg_4 = App.getContext().getDrawable(R.drawable.bg_orange);
        Drawable bg_5 = App.getContext().getDrawable(R.drawable.bg_redorange);
        Drawable bg_6 = App.getContext().getDrawable(R.drawable.bg_lightred);
        Drawable bg_7 = App.getContext().getDrawable(R.drawable.bg_red);
        Drawable bg_8 = App.getContext().getDrawable(R.drawable.bg_purple);
        Drawable[] drawables = {bg_0, bg_1, bg_2, bg_3, bg_4, bg_5, bg_6, bg_7, bg_8};

        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(drawables);
        view.setBackground(ctd);
        ctd.startTransition(4000, 8000); // 1 second transition, 3 second pause between transitions.
    }

    public static List<Routine> read(Context context) {
        List<Routine> existingRoutines;
        try {
            FileInputStream fileIn = context.openFileInput(FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(fileIn, StandardCharsets.UTF_8));
            existingRoutines = readRoutines(reader);
            reader.close();
            fileIn.close();
            return existingRoutines;
        } catch (FileNotFoundException f) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void write(Context context, Routine newRoutine, List<Routine> existingRoutines) {
        try {
            // Write file with new content
            FileOutputStream fileOut = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(fileOut, StandardCharsets.UTF_8));

            if(existingRoutines == null) {
                existingRoutines = new ArrayList<>();
            }
            existingRoutines.add(newRoutine);
            writeRoutinesArray(jsonWriter, existingRoutines);

            jsonWriter.close();
            fileOut.close();

            //display file saved message
            //Toast.makeText(context, "File saved successfully!",
            //        Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeRoutinesArray(JsonWriter writer, List<Routine> routines) throws IOException {
        writer.beginArray();
        for (Routine routine : routines) {
            writeRoutine(writer, routine);
        }
        writer.endArray();
    }

    public static void writeRoutine(JsonWriter writer, Routine routine) throws IOException {
        writer.beginObject();
        writer.name("id").value(routine.getId());
        writer.name("name").value(routine.getName());
        writer.name("time").value(routine.getTime());
        if(routine.getIconId() != 0) {
            writer.name("iconId").value(routine.getIconId());
        }
        writer.name("subRoutines");
        writeSubRoutines(writer, routine.getSubRoutines());
        writer.endObject();
    }

    public static void writeSubRoutines(JsonWriter writer, List<SubRoutine> subRoutines) throws IOException {
        writer.beginArray();
        for (SubRoutine value : subRoutines) {
            writer.beginObject();
            writer.name("description");
            writer.value(value.getDescription());
            writer.endObject();
        }
        writer.endArray();
    }

    public static List<Routine> readRoutines(JsonReader reader) throws IOException {
        List<Routine> existingRoutines = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            existingRoutines.add(readRoutine(reader));
        }
        reader.endArray();
        return existingRoutines;
    }

    public static Routine readRoutine(JsonReader reader) throws IOException {
        int id = -1;
        String name = null;
        int time = -1;
        int iconId = -1;
        List<SubRoutine> subRoutines = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("id")) {
                id = reader.nextInt();
            } else if (jsonName.equals("name")) {
                name = reader.nextString();
            } else if (jsonName.equals("time")) {
                time = reader.nextInt();
            } else if (jsonName.equals("iconId")) {
                iconId = reader.nextInt();
            } else if (jsonName.equals("subRoutines")) {
                subRoutines = readSubRoutines(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if(iconId != -1) {
            return new Routine(id, name, time, iconId, subRoutines);
        } else {
            return new Routine(id, name, time, 0, subRoutines);
        }

    }

    public static ArrayList<SubRoutine> readSubRoutines(JsonReader reader) throws IOException {
        ArrayList<SubRoutine> subList = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            subList.add(readSubRoutine(reader));
        }
        reader.endArray();
        return subList;
    }

    public static SubRoutine readSubRoutine(JsonReader reader) throws IOException {
        int id = -1;
        String description = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String jsonName = reader.nextName();
            if (jsonName.equals("id")) {
                id = reader.nextInt();
            } else if (jsonName.equals("description")) {
                description = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new SubRoutine(id, description);
    }

    public static AppStart checkAppStart(Context context) {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        // sharedPreferences.edit().clear().apply();

        AppStart appStart = AppStart.NORMAL;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appStart;
    }

    public static AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }

    public static int checkExp(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(EXPERIENCE_POINTS, 0);
    }

    public static void putExp(Context context, int points) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(EXPERIENCE_POINTS, points).apply();
    }

    public static int checkExpNeededForNextLevel(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(EXPERIENCE_NEEDED_FOR_LVL, 0);
    }

    public static void putExpNeededForNextLevel(Context context, int points) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(EXPERIENCE_NEEDED_FOR_LVL, points).apply();
    }

    public static int checkLevel(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        return sharedPreferences.getInt(LEVEL, 0);
    }

    public static void putLevel(Context context, int level) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        sharedPreferences.edit().putInt(LEVEL, level).apply();
    }
}
