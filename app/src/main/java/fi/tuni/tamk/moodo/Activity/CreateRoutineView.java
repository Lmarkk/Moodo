package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import fi.tuni.tamk.moodo.Classes.ListStatus;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Classes.RoutineAdapter;
import fi.tuni.tamk.moodo.Classes.SubRoutine;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;

public class CreateRoutineView extends AppCompatActivity implements ListStatus {

    private static final String FILE_NAME = "custom_routine_data.json";
    int totalRoutines;
    EditText subRoutineNameField;
    EditText routineNameField;
    ArrayList<String> listItems = new ArrayList<>();
    private Button saveCustomRoutines;
    private Routine editRoutine;
    private RoutineAdapter a;

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
        a = new RoutineAdapter(this, listItems, this);
        listView.setAdapter(a);

        routineNameField.addTextChangedListener(textWatcher);
        subRoutineNameField.addTextChangedListener(textWatcher);

        if(getIntent().hasExtra("curr_routine")) {
            editRoutine = (Routine) getIntent().getSerializableExtra("curr_routine");
            routineNameField.setText(editRoutine.getName());
            List<SubRoutine> subRoutines = editRoutine.getSubRoutines();
            for(int i = 0; i < subRoutines.size(); i++) {
                listItems.add(subRoutines.get(i).getDescription());
            }
            saveCustomRoutines.setEnabled(true);
            saveCustomRoutines.setAlpha(1);
        }
    }

    // insert data into list
    public void addItems(View v) {
        if(subRoutineNameField.getText().toString().length() > 0) {
            listItems.add(subRoutineNameField.getText().toString());
            a.notifyDataSetChanged();
            subRoutineNameField.setText("");
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
            if(editRoutine != null) {
                intent.putExtra("routine_status", 1);
                intent.putExtra("routine_id", getIntent().getIntExtra("routine_id", 0));
            }
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.create_routine_savebutton_warning), Toast.LENGTH_LONG).show();
        }

    }

    // Check if routine name field is changed and enable/disable from result
    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(listItems.size() > 0 && routineNameField.getText().toString().length() > 0) {
                saveCustomRoutines.setEnabled(true);
                saveCustomRoutines.setAlpha(1);
            } else {
                saveCustomRoutines.setEnabled(false);
                saveCustomRoutines.setAlpha(0.2f);
            }
        }
    };

    // Cancel creating custom routine
    public void cancelCustomRoutine(View v) {
        onBackPressed();
    }


    @Override
    public void checkStatus() {
        saveCustomRoutines.setEnabled(false);
        saveCustomRoutines.setAlpha(0.2f);
    }

    @Override
    public void modifySubRoutine(int pos, String newValue) {
        listItems.set(pos, newValue);
        a.notifyDataSetChanged();
    }
}
