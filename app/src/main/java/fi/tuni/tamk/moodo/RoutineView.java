package fi.tuni.tamk.moodo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ListIterator;

public class RoutineView extends AppCompatActivity implements CircleTimerView.CircleTimerListener {
    private TextView routineTitle;
    private ProgressBar progressBar;
    private ListIterator<SubRoutine> subRtnIterator;
    private Button completeSubRoutineBtn;
    private Button startRoutineBtn;
    private Button stopRoutineBtn;
    private boolean exitCountThread = false;
    private boolean doNotNotify = false;
    private NotificationManagerCompat notificationManager;
    private Routine routine;
    private ListView listView;
    private final int COLOR_DONE = Color.GREEN;
    private CircleTimerView mTimer;
    private int userTime;
    private Thread countThread;
    public static final int OPEN_NEW_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RoutineView", "onCreate");
        setContentView(R.layout.routine_view);
        routineTitle = findViewById(R.id.routine_title);
        //Instantiate notification manager
        notificationManager = NotificationManagerCompat.from(this);

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
        mTimer = (CircleTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        mTimer.setHintText("");
        // Set routine timer from routine time and user time to 0;
        userTime = 0;
        // Set subroutines to list view for specific routine
        listView = (ListView) findViewById(R.id.subroutine_list);
        ArrayAdapter<SubRoutine> adapter = new ArrayAdapter<>(this, R.layout.subroutine_list_item_layout, routine.getSubRoutines());
        listView.setAdapter(adapter);
        initializeBackgroundTransition();
    }

    public void startRoutine(View v) {
        if(mTimer.getCurrentTime() != 0) {
            ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mTimer.getLayoutParams();
            newLayoutParams.topMargin = 256;
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
            Toast.makeText(this, "Aseta aika ensin", Toast.LENGTH_LONG).show();
        }
    }

    public void completeSubRoutine(View v) {
        if(subRtnIterator.hasNext()) {
            completeSubRoutineBtn.setText(subRtnIterator.next().toString());
            progressBar.setProgress(progressBar.getProgress() + (100 / routine.getSubRoutines().size()));
        } else {
            completeSubRoutineBtn.setText("Valmis!");
            progressBar.setProgress(100);

            // start dialog with overview of completed routine
            final Dialog resultDialog = new Dialog(this);
            resultDialog.setContentView(R.layout.result_dialog);

            TextView dialogText = resultDialog.findViewById(R.id.result_dialog_text);
            dialogText.setText("Rutiiniin asetettu aika: " + formatTime(mTimer.getCurrentTime() + mTimer.getTotalTime()) + "\n");
            dialogText.append("Oma aikasi: " + formatTime(userTime) + "\n");
            dialogText.append("\n");
            if(userTime < (mTimer.getCurrentTime() + mTimer.getTotalTime() / 2)) {
                dialogText.append("Todella nopeaa toimintaa, hienosti tehty! Olithan huolellinen? \n");
            } else {
                dialogText.append("Hyvä, jatka samaan malliin! \n");
            }
            dialogText.append("\n");
            dialogText.append("Sait 10 pistettä! Voit käyttää pisteitäsi ulkoasut-ruudussa.");
            // give players points here...

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
        mTimer.setCircleButtonDisabled(false);
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mTimer.getLayoutParams();
        newLayoutParams.topMargin = 32;
        mTimer.setLayoutParams(newLayoutParams);
        countThread.interrupt();
        // reset timer and set timer text to full
        mTimer.pauseTimer();
        mTimer.setCurrentTime(0);
        mTimer.setTotalTime(0);
        userTime = 0;

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

    private void initializeBackgroundTransition() {
        View containerView = findViewById(R.id.root_view_routine);

        Drawable bg_0 = getResources().getDrawable(R.drawable.bg_blue);
        Drawable bg_1 = getResources().getDrawable(R.drawable.bg_blueorange);
        Drawable bg_2 = getResources().getDrawable(R.drawable.bg_greenyellow);
        Drawable bg_3 = getResources().getDrawable(R.drawable.bg_green);
        Drawable bg_4 = getResources().getDrawable(R.drawable.bg_orange);
        Drawable bg_5 = getResources().getDrawable(R.drawable.bg_redorange);
        Drawable bg_6 = getResources().getDrawable(R.drawable.bg_lightred);
        Drawable bg_7 = getResources().getDrawable(R.drawable.bg_red);
        Drawable bg_8 = getResources().getDrawable(R.drawable.bg_purple);
        Drawable[] drawables = {bg_0, bg_1, bg_2, bg_3, bg_4, bg_5, bg_6, bg_7, bg_8};

        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(drawables);
        containerView.setBackground(ctd);
        ctd.startTransition(4000, 8000); // 1 second transition, 3 second pause between transitions.
    }

    public void openSettings(View view) {
        doNotNotify = true;
        Intent intent = new Intent(this, Settings.class);
        startActivityForResult(intent, OPEN_NEW_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result OK.d.
        if (requestCode == OPEN_NEW_ACTIVITY) {
            doNotNotify = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("RoutineView", "onDestroy");
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
        return String.format("%02d", seconds /60) + ":" + String.format("%02d", seconds % 60);
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

    // Check for older android versions and redirect to onBackPressed method if needed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
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
