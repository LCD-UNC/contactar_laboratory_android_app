package com.rfdetke.digitriadlaboratory;

import com.rfdetke.digitriadlaboratory.entities.ScanConfiguration;

import java.util.Timer;
import java.util.TimerTask;

public abstract class ScanScheduler {
    private Timer activeTimer;
    private Timer inactiveTimer;

    private int windowCount;

    public ScanScheduler(final ScanConfiguration scanConfiguration) {
        activeTimer = new Timer();
        inactiveTimer = new Timer();
        long period = scanConfiguration.activeTime + scanConfiguration.inactiveTime;
        windowCount = 0;

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
                }
            }
        };

        activeTimer.scheduleAtFixedRate(activeTask, 0, period);
        inactiveTimer.scheduleAtFixedRate(inactiveTask, scanConfiguration.activeTime, period);
    }

    protected abstract void registerScanDataBucket();

    protected abstract void unregisterScanDataBucket();
}
