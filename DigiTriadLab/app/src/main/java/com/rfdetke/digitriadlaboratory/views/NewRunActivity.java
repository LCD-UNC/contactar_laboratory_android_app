package com.rfdetke.digitriadlaboratory.views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rfdetke.digitriadlaboratory.AlarmReceiver;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao.StartDuration;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewRunActivity extends AppCompatActivity {
    public static final String EXTRA_RUN_ID = "com.rfdetke.digitriadlaboratory.RUN_ID";

    private static final int MINUTES_MARGIN = 1;
    private static final int MILLIS_CONSTANT = 1000;
    private static final int SECONDS_CONSTANT = 60;
    private static final int MILLIS_MARGIN = MINUTES_MARGIN*SECONDS_CONSTANT*MILLIS_CONSTANT;

    private DatePicker startDate;
    private TimePicker startTime;
    private SimpleDateFormat simpleDateFormat;
    private RunRepository runRepository;
    private long experimentId;
    private AlarmManager alarmManager;
    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_run);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle(getString(R.string.new_run));

        experimentId = getIntent().getLongExtra(ExperimentListAdapter.EXTRA_ID, 0);
        AppDatabase database = DatabaseSingleton.getInstance(getApplicationContext());
        runRepository = new RunRepository(database);

        startDate = findViewById(R.id.datePicker);
        startDate.setMinDate(new Date().getTime());
        startTime = findViewById(R.id.timePicker);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            String date = String.format(Locale.ENGLISH, "%d", startDate.getDayOfMonth())+"-"+
                    String.format(Locale.ENGLISH, "%d",startDate.getMonth()+1)+"-"+
                    startDate.getYear();

            String time = startTime.getHour()+":"+startTime.getMinute();
            String datetime = date+" "+time;
            Date selectedDate = null;
            try {
                selectedDate = simpleDateFormat.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(validateStartDatetime(selectedDate)) {
                long runId = saveRun(selectedDate);
                Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                alarmIntent.putExtra(EXTRA_RUN_ID, runId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)runId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedDate.getTime(), pendingIntent);
                finish();
            } else {
                Toast.makeText(this, R.string.invalid_datetime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    private long saveRun(Date selectedDate) {
        long number = runRepository.getLastRunForExperiment(experimentId)+1;
        return runRepository.insert(new Run(selectedDate, number, RunStateEnum.SCHEDULED.name(), experimentId));
    }

    private boolean validateStartDatetime(Date selectedDate) {

        if(selectedDate != null) {
            if(selectedDate.before(new Date())) {
                return false;
            } else {
                long maxDurationForNewRun = runRepository.getMaxDurationForExperiment(experimentId);
                Date selectedEstimatedEnd = new Date(selectedDate.getTime()+(maxDurationForNewRun*MILLIS_CONSTANT)+MILLIS_MARGIN);

                List<StartDuration> startDurations = runRepository.getCurrentScheduledOrRunningStartAndDuration();
                for(StartDuration startDuration : startDurations) {
                    Date estimatedEnd = new Date(startDuration.start.getTime()+(startDuration.duration*MILLIS_CONSTANT)+MILLIS_MARGIN);
                    if((selectedEstimatedEnd.after(startDuration.start) && (selectedEstimatedEnd.before(estimatedEnd))) ||
                       (selectedDate.after(startDuration.start) && selectedEstimatedEnd.before(estimatedEnd)) ||
                       (selectedDate.after(startDuration.start) && selectedDate.before(estimatedEnd))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}