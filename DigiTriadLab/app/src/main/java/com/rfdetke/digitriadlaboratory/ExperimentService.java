package com.rfdetke.digitriadlaboratory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.rfdetke.digitriadlaboratory.scanners.ScanObserver;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;

import java.util.Map;

public class ExperimentService extends Service implements ScanObserver {

    Map<String, Boolean> doneMap;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: Instanciar Schedulers
        // TODO: "registrar" Schedulers en el doneMap con registerScanner
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void update(Object scannerKey) {
        doneMap.put((String)scannerKey, Boolean.TRUE);
    }

    private void registerScanner(ScanScheduler scanner) {
        scanner.addObserver(this);
        doneMap.put(scanner.getKey(), Boolean.FALSE);
    }
}
