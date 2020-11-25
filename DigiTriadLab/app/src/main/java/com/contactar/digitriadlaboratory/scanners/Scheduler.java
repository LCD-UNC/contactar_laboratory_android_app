package com.contactar.digitriadlaboratory.scanners;

import android.content.Context;
import android.util.Log;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.WindowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Scheduler implements ObservableTask {

    private static final int initialDelay = 500;

    protected long runId;
    protected Context context;

    protected final WindowRepository windowRepository;
    protected List<TaskObserver> observers;

    protected String key;

    private Timer activeTimer;
    private Timer inactiveTimer;

    protected int windowCount;

    public Scheduler(long runId, long randomTime, final WindowConfiguration windowConfiguration, Context context,
                     AppDatabase database) {

        this.runId = runId;
        this.context = context;
        this.windowRepository = new WindowRepository(database);
        activeTimer = new Timer();
        inactiveTimer = new Timer();
        long period =  (windowConfiguration.activeTime + windowConfiguration.inactiveTime)*1000;
        long initialRandomTime = 0;

        if(randomTime > 0) {
            initialRandomTime = ThreadLocalRandom.current().nextInt(0, (int)randomTime*60)*1000;
        }

        windowCount = 0;
        observers = new ArrayList<>();

        TimerTask activeTask = new TimerTask() {
            @Override
            public void run() {
                windowCount += 1;
                startTask();
            }
        };

        TimerTask inactiveTask = new TimerTask() {
            @Override
            public void run() {
                endTask();
                if (windowCount == windowConfiguration.windows) {
                    activeTimer.cancel();
                    activeTimer.purge();
                    inactiveTimer.cancel();
                    inactiveTimer.purge();
                    setDone();
                } else {
                    Log.d("Scheduler", "Still have work to do...");
                }
            }
        };

        activeTimer.scheduleAtFixedRate(activeTask, initialDelay+initialRandomTime, period);
        inactiveTimer.scheduleAtFixedRate(inactiveTask, (windowConfiguration.activeTime*1000)+initialDelay+initialRandomTime, period);
    }

    public String getKey() {
        return key;
    }

    protected abstract void startTask();

    protected abstract void endTask();

    public void stop(){
        activeTimer.cancel();
        activeTimer.purge();
        inactiveTimer.cancel();
        inactiveTimer.purge();
    }


    @Override
    public void addObserver(TaskObserver taskObserver) {
        this.observers.add(taskObserver);
    }

    @Override
    public void removeObserver(TaskObserver taskObserver) {
        this.observers.remove(taskObserver);
    }

    @Override
    public void setDone() {
        for (TaskObserver observer: this.observers) {
            observer.update(this.key);
        }
    }
}
