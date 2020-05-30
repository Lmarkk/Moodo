package fi.tuni.tamk.moodo.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.Adapter;
import fi.tuni.tamk.moodo.Classes.LocaleHelper;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.SubRoutine;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MoodoApp extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Routine> routineList;
    private ProgressBar levelProgressBar;
    private TextView levelProgressBarExpText;
    private TextView levelProgressBarLvlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodo_app);
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

        // Create view and routinelist
        listView = findViewById(R.id.routine_list);
        routineList = new ArrayList<>();

        // Load routine data from .json file if first time startup
        // Set experience needed for first levelup to 20 points
        if(Util.checkAppStart(this) == Util.AppStart.FIRST_TIME) {
            Util.putExpNeededForNextLevel(this, 20);
            loadRoutineData();
        }

        // Instantiate and update progress bar and text for user level
        levelProgressBar = findViewById(R.id.progressBarLevel);
        levelProgressBarExpText = findViewById(R.id.progressBarExpText);
        levelProgressBarLvlText = findViewById(R.id.progressBarLevelText);
        updateProgressBar();

        // Add routines from local storage file to list
        ArrayList<Routine> customRoutines = (ArrayList<Routine>) Util.read(this);
        if(customRoutines != null) {
            routineList.addAll(customRoutines);
            if(getIntent().getIntExtra("routine_status", 0) == 1) {
                routineList.remove(getIntent().getIntExtra("routine_id", 0));
                Util.write(this, null, routineList);
            }
        }

        // Add list of routines to the list view
        // ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        Adapter adapter = new Adapter(this, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                startActivity(intent);
            }
        });

        for(int i = 0; i < adapter.getCount(); i++ ) {
            adapter.getView(i, null, listView);
        }

        Util.initializeBackgroundTransition(findViewById(R.id.root_view_moodo));

        // Show dialog for modifying list items from long click
        listView.setOnItemLongClickListener((parent, view, pos, id) -> {
            final int which_item = pos;
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.sub_dialog_title))
                    .setMessage(getString(R.string.sub_dialog_msg))
                    // Edit list item
                    .setPositiveButton(getString(R.string.sub_dialog_edit), (dialog, which) -> {
                        Intent intent = new Intent(this, CreateRoutineView.class);
                        intent.putExtra("curr_routine", routineList.get(which_item));
                        intent.putExtra("routine_id", which_item);
                        intent.putExtra("totalRoutines", routineList.size() + 1);
                        startActivity(intent);

                    })
                    // Delete list item
                    .setNeutralButton(getString(R.string.sub_routine_delete), (dialog, which) -> {
                        routineList.remove(which_item);
                        adapter.notifyDataSetChanged();
                        Util.write(this, null, routineList);
                    })
                    // Cancel action
                    .setNegativeButton(getString(R.string.sub_routine_cancel), null)
                    .show().getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.RED);
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressBar();
    }

    public void updateProgressBar() {
        levelProgressBar.setProgress(Util.checkExp(this));
        levelProgressBar.setMax(Util.checkExpNeededForNextLevel(this));
        levelProgressBarExpText.setText(Util.checkExp(this) + "/" + Util.checkExpNeededForNextLevel(this));
        levelProgressBarLvlText.setText(getString(R.string.user_level) + " " + Util.checkLevel(this));
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // Loads JSON data from json file and convert it to string
    private void loadRoutineData() {
        Resources res = getResources();
        InputStream is = null;
        if(LocaleHelper.getLanguage(this).equals("fi")) {
            is = res.openRawResource(R.raw.routine_data_fin);
        } else {
            is = res.openRawResource(R.raw.routine_data_en);
        }
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    // Parse string into routine and subroutines for adding into local storage
    private void parseJson(String jsonString) {
        // Temp array for storing subroutines
        ArrayList<SubRoutine> subRoutines = new ArrayList<>();
        try {
            // Json file root object
            JSONObject root = new JSONObject(jsonString);
            // Routine JsonArray
            JSONArray routineArray = root.getJSONArray("routines");
            // Loop through routine array and add routines to routine list
            for(int i = 0; i < routineArray.length(); i++) {
                // Get current routine and add to array
                JSONObject currRoutine = routineArray.getJSONObject((i));
                Routine tempRoutine = new Routine(currRoutine.getInt("id"),
                        currRoutine.getString("name"),
                        currRoutine.getInt("time"),
                        currRoutine.getInt("iconId"));
                System.out.println(tempRoutine.getIconId());
                // Loop through subroutine array included inside every routine JSONObject
                JSONArray subroutineArray = currRoutine.getJSONArray("subroutines");
                for(int j = 0; j < subroutineArray.length(); j++) {
                    // Get current subroutine and add to list
                    JSONObject currSubRoutine = subroutineArray.getJSONObject(j);
                    subRoutines.add(new SubRoutine(currRoutine.getInt("id"),
                            currSubRoutine.getString("description")));
                }
                // After looping through whole data add temp list to routine's subroutine list
                // and clear the subroutines list
                tempRoutine.setSubRoutines(subRoutines);
                // Write temp routine to storage
                Util.write(this, tempRoutine, Util.read(this));
                subRoutines.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openCreateRoutineView(View v) {
        Intent intent = new Intent(this, CreateRoutineView.class);
        intent.putExtra("totalRoutines", routineList.size() + 1);
        startActivity(intent);
    }
}
