package com.rfdetke.digitriadlaboratory.scanners.gps;

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

import com.rfdetke.digitriadlaboratory.database.entities.GpsRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class GpsDataBucket implements DataBucket {
    private final long sampleId;
    LocationManager locationManager;
    Context context;
    List<Object> records = new ArrayList<>();

    public GpsDataBucket(long sId, Context context) {
        this.sampleId = sId;
        this.context = context;
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        myLocationListener locationListener = new myLocationListener();
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity) this.context).startActivity(settingsIntent);
        }
        if (locationManager != null) {
            Log.d("GPS", "listener created!");
            GnssMeasurementsEvent.Callback gnssMeasurementsEventListener = new GnssMeasurementsEvent.Callback() {

                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) {
                    Log.d("GPS", "onGnssMeasurementsReceived: invoked!");
                    /*super.onGnssMeasurementsReceived(event);
                    for (GnssMeasurement measurement : event.getMeasurements())
                    {
                        records.add(new GpsRecord(measurement, sampleId));
                    }*/
                }

                @Override
                public void onStatusChanged(int status) {
                    super.onStatusChanged(status);
                }
            };
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this.context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            }
            if(locationManager.registerGnssMeasurementsCallback(gnssMeasurementsEventListener))
            {
                Log.d("GPS", "listener registered");
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);
        }
    }

    @Override
    public List<Object> getRecordsList() {

        return records;
    }


}
