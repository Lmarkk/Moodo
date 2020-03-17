package fi.tuni.tamk.moodo;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Iterator;
import java.util.ListIterator;

public class RoutineView extends AppCompatActivity {
    private TextView routineTitle;
    private ProgressBar progressBar;
    private ListIterator<SubRoutine> subRtnIterator;
    private Button completeSubRoutineBtn;
    private Button startRoutineBtn;
    private Button stopRoutineBtn;
    private TextView timerText;
    private CountDownTimer routineTimer = null;
    private Routine routine;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RoutineView", "onCreate");
        setContentView(R.layout.routine_view);
        routineTitle = findViewById(R.id.routine_title);
        timerText = findViewById(R.id.routine_timer);

        completeSubRoutineBtn = findViewById(R.id.completeSubRoutineButton);
        startRoutineBtn = findViewById(R.id.startButton);
        stopRoutineBtn = findViewById(R.id.stopButton);

        // Set routine variable from sent intent, set routine title from routine name
        routine = (Routine) getIntent().getSerializableExtra("routine");
        routineTitle.setText(routine.getName());

        subRtnIterator = routine.getSubRoutines().listIterator();
        subRtnIterator.next();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        // Set routine timer from routine time
        timerText.setText(String.format("%02d", routine.getTime() /60) + ":" + String.format("%02d", routine.getTime() % 60));

        // Set subroutines to list view for specific routine
        listView = (ListView) findViewById(R.id.subroutine_list);
        ArrayAdapter<SubRoutine> adapter = new ArrayAdapter<>(this, R.layout.subroutine_list_item_layout, routine.getSubRoutines());
        listView.setAdapter(adapter);

    }

    public void startRoutine(View v) {
        startTimer(routine.getTime(), timerText);
        completeSubRoutineBtn.setText(routine.getSubRoutines().get(0).toString());
        completeSubRoutineBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        stopRoutineBtn.setVisibility(View.VISIBLE);

        startRoutineBtn.setVisibility(View.GONE);
        routineTitle.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    public void completeSubRoutine(View v) {
        if(subRtnIterator.hasNext()) {
            completeSubRoutineBtn.setText(subRtnIterator.next().toString());
            progressBar.setProgress(progressBar.getProgress() + (100 / routine.getSubRoutines().size()));
        } else {
            completeSubRoutineBtn.setText("Valmis!");
            progressBar.setProgress(100);
            routineTimer.cancel();
            // start dialog with overview of completed routine
            final Dialog resultDialog = new Dialog(this);
            resultDialog.setContentView(R.layout.result_dialog);

            TextView dialogText = resultDialog.findViewById(R.id.result_dialog_text);

            Button dialogButton = resultDialog.findViewById(R.id.result_dialog_dismiss_button);
            dialogButton.setOnClickListener(v1 -> {
                resultDialog.dismiss();
                stopRoutine(v1);
            });

            resultDialog.show();
        }
    }

    public void stopRoutine(View v) {
        // reset timer and set timer text to full
        routineTimer.cancel();
        timerText.setText(String.format("%02d", routine.getTime() /60) + ":" + String.format("%02d", routine.getTime() % 60));

        //reset visibilities back to starting position
        completeSubRoutineBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        stopRoutineBtn.setVisibility(View.GONE);

        startRoutineBtn.setVisibility(View.VISIBLE);
        routineTitle.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        // reset progress bar progress
        progressBar.setProgress(0);

        // reset subroutine iterator
        subRtnIterator = routine.getSubRoutines().listIterator();
        subRtnIterator.next();
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
