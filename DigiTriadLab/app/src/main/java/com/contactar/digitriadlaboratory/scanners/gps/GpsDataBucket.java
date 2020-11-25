package com.contactar.digitriadlaboratory.scanners.gps;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.contactar.digitriadlaboratory.database.entities.GpsRecord;
import com.contactar.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class GpsDataBucket extends GnssMeasurementsEvent.Callback implements DataBucket {

    private long sampleId;
    LocationManager locationManager;
    Context context;
    List<Object> records;

    public GpsDataBucket(Context context) {
        this.context = context;
        this.records = new ArrayList<>();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public void setSampleId(long sampleId) {
        this.sampleId = sampleId;
        try {
            Log.d("GPS", "Registered!");
            locationManager.registerGnssMeasurementsCallback(this);
        } catch (SecurityException e) {
            Log.d("GPS", "Failed");
        }
    }

    public void stop() {
        locationManager.unregisterGnssMeasurementsCallback(this);
    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {
        Log.d("GPS", "GNSS received");
        super.onGnssMeasurementsReceived(event);
        for (GnssMeasurement measurement : event.getMeasurements())
        {
            records.add(new GpsRecord(measurement, sampleId));
        }
    }
}
