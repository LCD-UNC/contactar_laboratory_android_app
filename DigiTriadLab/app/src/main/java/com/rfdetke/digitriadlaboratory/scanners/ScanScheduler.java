package com.rfdetke.digitriadlaboratory.scanners;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.ScanConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class ScanScheduler implements ObservableScanner{

    private static final int initialDelay = 500;

    protected long runId;
    protected Context context;

    protected final SampleDao sampleDao;
    protected final SourceTypeDao sourceTypeDao;
    protected List<ScanObserver> observers;

    protected String key;

    private Timer activeTimer;
    private Timer inactiveTimer;

    private int windowCount;

    public ScanScheduler(long runId, final ScanConfiguration scanConfiguration, Context context,
                         SampleDao sampleDao, SourceTypeDao sourceTypeDao) {

        this.runId = runId;
        this.context = context;
        this.sampleDao = sampleDao;
        this.sourceTypeDao = sourceTypeDao;
        activeTimer = new Timer();
        inactiveTimer = new Timer();
        long period =  (scanConfiguration.activeTime + scanConfiguration.inactiveTime)*1000;
        windowCount = 0;
        observers = new ArrayList<>();

        TimerTask activeTask = new TimerTask() {
            @Override
            public void run() {
                windowCount += 1;
                registerScanDataBucket();
            }
        };

        TimerTask inactiveTask = new TimerTask() {
            @Override
            public void run() {
                unregisterScanDataBucket();
                if (windowCount == scanConfiguration.windows) {
                    activeTimer.cancel();
                    activeTimer.purge();
                    inactiveTimer.cancel();
                    inactiveTimer.purge();
                    setDoneScanning();
                }
            }
        };

        activeTimer.scheduleAtFixedRate(activeTask, initialDelay, period);
        inactiveTimer.scheduleAtFixedRate(inactiveTask, (scanConfiguration.activeTime*1000)+initialDelay, period);

    }

    public String getKey() {
        return key;
    }

    protected abstract void registerScanDataBucket();

    protected abstract void unregisterScanDataBucket();

    @Override
    public void addObserver(ScanObserver scanObserver) {
        this.observers.add(scanObserver);
    }

    @Override
    public void removeObserver(ScanObserver scanObserver) {
        this.observers.remove(scanObserver);
    }

    @Override
    public void setDoneScanning() {
        for (ScanObserver observer: this.observers) {
            observer.update(this.key);
        }
    }
}
