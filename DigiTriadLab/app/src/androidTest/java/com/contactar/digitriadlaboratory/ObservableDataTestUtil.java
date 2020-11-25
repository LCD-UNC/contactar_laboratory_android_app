package com.contactar.digitriadlaboratory;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.contactar.digitriadlaboratory.scanners.TaskObserver;
import com.contactar.digitriadlaboratory.scanners.Scheduler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ObservableDataTestUtil {
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

    public static String getValue(final Scheduler scheduler, long duration) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        TaskObserver observer = new TaskObserver() {
            @Override
            public void update(Object scannerKey) {
                data[0] = scannerKey;
                latch.countDown();
                scheduler.removeObserver(this);
            }

        };
        scheduler.addObserver(observer);
        latch.await(duration, TimeUnit.MILLISECONDS);

        return (String) data[0];
    }
}
