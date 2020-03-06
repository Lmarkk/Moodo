package fi.tuni.tamk.moodo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RoutineView extends AppCompatActivity {
    private TextView timerText;
    private CountDownTimer routineTimer = null;
    private Routine routine;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RoutineView", "onCreate");
        setContentView(R.layout.routine_view);
        TextView routineTitle = findViewById(R.id.routine_title);
        timerText = findViewById(R.id.routine_timer);

        // Set routine variable from sent intent, set routine title from routine name
        routine = (Routine) getIntent().getSerializableExtra("routine");
        routineTitle.setText(routine.getName());

        // Set routine timer from routine time
        timerText.setText(String.format("%02d", routine.getTime() /60) + ":" + String.format("%02d", routine.getTime() % 60));

        // Set subroutines to list view for specific routine
        listView = (ListView) findViewById(R.id.subroutine_list);
        ArrayAdapter<SubRoutine> adapter = new ArrayAdapter<>(this, R.layout.subroutine_list_item_layout, routine.getSubRoutines());
        listView.setAdapter(adapter);

    }

    public void startRoutine(View v) {
        startTimer(routine.getTime(), timerText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("RoutineView", "onDestroy");
        listView.setAdapter(null);
        if(routineTimer != null) {
            routineTimer.cancel();
        }
    }

    public void startTimer(int seconds, final TextView textView) {

        routineTimer = new CountDownTimer(seconds* 1000+1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                textView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {}
        }.start();
    }

    public int millisToMinutes(long millis) {
        return millisToSeconds(millis) / 60;
    }

    public int millisToSeconds(long millis) {
        return (int) millis / 1000;
    }

}
