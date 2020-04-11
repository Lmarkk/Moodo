package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.SubRoutine;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MoodoApp extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Routine> routineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MoodoApp", "onCreate");
        setContentView(R.layout.moodo_app);

        // Create view and addroutines to list
        listView = findViewById(R.id.routine_list);
        routineList = new ArrayList<>();

        // Load routine data from .json file
        loadRoutineData();

        // Add list ofroutines to the list view
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(this, R.layout.list_item, routineList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Routine listItem = (Routine) listView.getItemAtPosition(position);
                //Log.d("MoodoApp", "Subroutines: " + listItem.getSubRoutines().toString());
                Intent intent = new Intent(getApplicationContext(), RoutineView.class);
                // Add clicked list routine to be displayed in RoutineView
                intent.putExtra("routine", listItem);
                startActivity(intent);
            }
        });

        Util.initializeBackgroundTransition(findViewById(R.id.root_view_moodo));
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void loadRoutineData() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.routine_data);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        ArrayList<SubRoutine> subRoutines = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(s);
            JSONArray routineArray = root.getJSONArray("routines");
            for(int i = 0; i < routineArray.length(); i++) {
                JSONObject cr = routineArray.getJSONObject((i));
                System.out.println(routineArray.getJSONObject(i));
                Routine tempRoutine = new Routine(cr.getInt("id"), cr.getString("name"), cr.getInt("time"));
                routineList.add(tempRoutine);
                JSONArray subroutineArray = cr.getJSONArray("subroutines");
                for(int j = 0; j < subroutineArray.length(); j++) {
                    JSONObject csr = subroutineArray.getJSONObject(j);
                    subRoutines.add(new SubRoutine(cr.getInt("id"), csr.getString("description")));
                }
                tempRoutine.setSubRoutines(subRoutines);
                subRoutines.clear();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
