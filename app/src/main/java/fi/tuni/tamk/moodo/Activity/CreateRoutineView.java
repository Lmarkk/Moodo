package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.SubRoutine;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

public class CreateRoutineView extends AppCompatActivity {

    private static final String FILE_NAME = "custom_routine_data.json";
    int totalRoutines;
    EditText subRoutineNameField;
    EditText routineNameField;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private Button saveCustomRoutines;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_routine_view);

        totalRoutines = getIntent().getIntExtra("totalRoutines", 0);
        routineNameField = findViewById(R.id.create_routine_namefield);
        subRoutineNameField = findViewById(R.id.create_routine_subnamefield);
        saveCustomRoutines = findViewById(R.id.save_custom_routines);
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_create_routine));

        ListView listView = findViewById(R.id.create_routine_listview);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        listView.setAdapter(adapter);
    }

    // insert data into list
    public void addItems(View v) {
        if(subRoutineNameField.getText().toString().length() > 0) {
            listItems.add(subRoutineNameField.getText().toString());
            adapter.notifyDataSetChanged();
            subRoutineNameField.setText("");
            saveCustomRoutines.setEnabled(true);
            saveCustomRoutines.setAlpha(1);
        } else {
            Toast.makeText(this, getString(R.string.create_routine_empty_subroutines_warning), Toast.LENGTH_LONG).show();
        }
    }

    // save new routine
    public void saveNewRoutine(View v) {
        if(routineNameField.getText().toString().length() > 0 && listItems.size() > 0) {
            int subRoutineId = 1;
            Routine newRoutine = new Routine(totalRoutines, routineNameField.getText().toString(), 300);
            for(String str: listItems) {
                newRoutine.addSubRoutine(new SubRoutine(subRoutineId, str));
                subRoutineId++;
            }
            Util.write(this, newRoutine, Util.read(this));
            Intent intent = new Intent(this, MoodoApp.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.create_routine_savebutton_warning), Toast.LENGTH_LONG).show();
        }

    }

    // Cancel creating custom routine
    public void cancelCustomRoutine(View v) {
        onBackPressed();
    }


}
