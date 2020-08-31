package com.rfdetke.digitriadlaboratory.scanners;

import android.content.Context;
import android.util.Log;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.WindowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Scheduler implements ObservableScanner{

    private static final int initialDelay = 500;

    protected long runId;
    protected Context context;

    protected final WindowRepository windowRepository;
    protected List<ScanObserver> observers;

    protected String key;

    private Timer activeTimer;
    private Timer inactiveTimer;

    private int windowCount;

    public Scheduler(long runId, final WindowConfiguration windowConfiguration, Context context,
                     AppDatabase database) {

        this.runId = runId;
        this.context = context;
        this.windowRepository = new WindowRepository(database);
        activeTimer = new Timer();
        inactiveTimer = new Timer();
        long period =  (windowConfiguration.activeTime + windowConfiguration.inactiveTime)*1000;
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

        activeTimer.scheduleAtFixedRate(activeTask, initialDelay, period);
        inactiveTimer.scheduleAtFixedRate(inactiveTask, (windowConfiguration.activeTime*1000)+initialDelay, period);
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
    public void addObserver(ScanObserver scanObserver) {
        this.observers.add(scanObserver);
    }

    @Override
    public void removeObserver(ScanObserver scanObserver) {
        this.observers.remove(scanObserver);
    }

    @Override
    public void setDone() {
        for (ScanObserver observer: this.observers) {
            observer.update(this.key);
        }
    }
}
