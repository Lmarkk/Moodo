package fi.tuni.tamk.moodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class RoutineView extends AppCompatActivity {
    private Routine routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_view);
        TextView routineTitle = findViewById(R.id.routine_title);

        routine = (Routine) getIntent().getSerializableExtra("routine");
        routineTitle.setText(routine.getName());
    }

}
