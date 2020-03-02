package fi.tuni.tamk.moodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RoutineView extends AppCompatActivity {
    private Routine routine;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RoutineView", "onCreate");
        setContentView(R.layout.routine_view);
        TextView routineTitle = findViewById(R.id.routine_title);

        // Set routine title from sent intent
        routine = (Routine) getIntent().getSerializableExtra("routine");
        routineTitle.setText(routine.getName());

        // Set subroutines to list view for specific routine
        listView = (ListView) findViewById(R.id.subroutine_list);
        ArrayAdapter<SubRoutine> adapter = new ArrayAdapter<>(this, R.layout.subroutine_list_item_layout, routine.getSubRoutines());
        listView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("RoutineView", "onDestroy");
        listView.setAdapter(null);
    }
}
