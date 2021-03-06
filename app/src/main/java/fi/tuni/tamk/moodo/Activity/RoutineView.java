package fi.tuni.tamk.moodo.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.app.Dialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import fi.tuni.tamk.moodo.Classes.CircleTimerView;
import fi.tuni.tamk.moodo.Classes.Util;
import fi.tuni.tamk.moodo.R;
import fi.tuni.tamk.moodo.Classes.Routine;
import fi.tuni.tamk.moodo.Service.TimerService;
import fi.tuni.tamk.moodo.Classes.SubRoutine;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ListIterator;
import java.util.Locale;

public class RoutineView extends AppCompatActivity implements CircleTimerView.CircleTimerListener {
    // Static variables
    private static final int COLOR_DONE = Color.GREEN;
    private static final int OPEN_NEW_ACTIVITY = 1;

    private ListIterator<SubRoutine> subRtnIterator;
    private NotificationManagerCompat notificationManager;
    private Thread countThread;

    // Android view elements
    private TextView routineTitle;
    private ProgressBar progressBar;
    private Button completeSubRoutineBtn;
    private Button startRoutineBtn;
    private ImageButton stopRoutineBtn;
    private ListView listView;
    // Custom view element
    private CircleTimerView mTimer;

    // Class variables
    private int userTime;
    private boolean exitCountThread = false;
    private boolean doNotNotify = false;
    private Routine routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_view);
        routineTitle = findViewById(R.id.routine_title);
        //Instantiate notification manager
        notificationManager = NotificationManagerCompat.from(this);

        completeSubRoutineBtn = findViewById(R.id.completeSubRoutineButton);
        startRoutineBtn = findViewById(R.id.startButton);
        stopRoutineBtn = findViewById(R.id.backButton);

        // Set routine variable from sent intent, set routine title from routine name
        routine = (Routine) getIntent().getSerializableExtra("routine");
        routineTitle.setText(routine.getName());
        subRtnIterator = routine.getSubRoutines().listIterator();
        subRtnIterator.next();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        mTimer = (CircleTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        mTimer.setHintText("");
        // Set routine timer from routine time and user time to 0;
        userTime = 0;
        // Set subroutines to list view for specific routine
        listView = (ListView) findViewById(R.id.subroutine_list);
        ArrayAdapter<SubRoutine> adapter = new ArrayAdapter<>(this, R.layout.subroutine_list_item_layout, routine.getSubRoutines());
        listView.setAdapter(adapter);
        Util.initializeBackgroundTransition(findViewById(R.id.root_view_routine));
    }

    public void startRoutine(View v) {
        if(mTimer.getCurrentTime() != 0) {
            ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mTimer.getLayoutParams();
            newLayoutParams.topMargin = 190;
            mTimer.setLayoutParams(newLayoutParams);
            mTimer.setCircleButtonDisabled(true);
            mTimer.startTimer();
            startCountThread();
            progressBar.getProgressDrawable().setColorFilter(null);
            completeSubRoutineBtn.setText(routine.getSubRoutines().get(0).toString());
            completeSubRoutineBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            stopRoutineBtn.setVisibility(View.VISIBLE);

            startRoutineBtn.setVisibility(View.GONE);
            routineTitle.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, getString(R.string.timer_warning), Toast.LENGTH_LONG).show();
        }
    }

    public void completeSubRoutine(View v) {
        if(subRtnIterator.hasNext()) {
            completeSubRoutineBtn.setText(subRtnIterator.next().toString());
            progressBar.setProgress(progressBar.getProgress() + (100 / routine.getSubRoutines().size()));
        } else {
            completeSubRoutineBtn.setText(getString(R.string.routine_done));
            progressBar.setProgress(100);

            // start dialog with overview of completed routine
            final Dialog resultDialog = new Dialog(this);
            resultDialog.setContentView(R.layout.result_dialog);

            TextView dialogText = resultDialog.findViewById(R.id.result_dialog_text);
            dialogText.setText(String.format("%s%s\n", getString(R.string.routine_time_set) + " ", formatTime(mTimer.getCurrentTime() + mTimer.getTotalTime())));
            dialogText.append("\n" + getString(R.string.user_time) + " " + formatTime(userTime) + "\n\n");

            dialogText.append("\n\n" + getString(R.string.routine_scores));

            // User is given points, point and level up status is checked
            Util.putExp(this, Util.checkExp(this) + 10);
            if(Util.checkExp(this) >= Util.checkExpNeededForNextLevel(this)) {
                Util.putLevel(this, Util.checkLevel(this) +1);
                Util.putExp(this, 0);
                Util.putExpNeededForNextLevel(this, Util.checkExpNeededForNextLevel(this) + 10);
                dialogText.append("\n\n" + getString(R.string.result_dialog_levelup));
            }

            dialogText.append("\n\n" + getString(R.string.result_dialog_feedback));

            Button dialogButton = resultDialog.findViewById(R.id.result_dialog_dismiss_button);
            dialogButton.setOnClickListener(v1 -> {
                resultDialog.dismiss();
                stopRoutine(v1);
            });
            resultDialog.show();
            progressBar.getProgressDrawable().setColorFilter(COLOR_DONE, PorterDuff.Mode.SRC_IN);
            mTimer.pauseTimer();
        }
    }

    public void stopRoutine(View v) {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result OK.d.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_NEW_ACTIVITY) {
            doNotNotify = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listView.setAdapter(null);
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
        notificationManager.cancelAll();
        exitCountThread = true;
        mTimer.pauseTimer();
    }

    public void startCountThread() {
        countThread = new Thread() {
            @Override
            public void run() {
                try {
                    while(progressBar.getProgress() != 100 && !exitCountThread) {
                        sleep(1000);
                        userTime++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        countThread.start();
    }

    public String formatTime(int seconds) {
        // Use locale to avoid lint error
        return String.format(Locale.ENGLISH,"%02d", seconds /60) + ":" + String.format(Locale.ENGLISH,"%02d", seconds % 60);
    }

    public int millisToSeconds(long millis) {
        return (int) millis / 1000;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Start timer service
        if(mTimer.ismStarted() && !doNotNotify) {
            Intent serviceIntent = new Intent(this, TimerService.class);
            startService(serviceIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // stop timer service
        Intent serviceIntent = new Intent(this, TimerService.class);
        notificationManager.cancelAll();
        stopService(serviceIntent);

    }

    // Prevent notifications when back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doNotNotify = true;
    }

    @Override
    public void onTimerStop() {

    }

    @Override
    public void onTimerStart(int time) {
    }

    @Override
    public void onTimerPause(int time) {

    }

    @Override
    public void onTimerTimingValueChanged(int time) {

    }

    @Override
    public void onTimerSetValueChanged(int time) {

    }

    @Override
    public void onTimerSetValueChange(int time) {

    }
}
