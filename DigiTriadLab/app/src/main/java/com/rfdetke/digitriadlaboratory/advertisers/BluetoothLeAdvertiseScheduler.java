package com.rfdetke.digitriadlaboratory.advertisers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;

public class BluetoothLeAdvertiseScheduler extends Scheduler {

    private AdvertisingSetParameters parameters;
    private AdvertiseData data;
    private BluetoothLeAdvertiser advertiser;
    private AdvertisingSetCallback callback;

    public BluetoothLeAdvertiseScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration,
                                         AdvertiseConfiguration advertiseConfiguration,
                                         Context context, AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);

        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        parameters = (new AdvertisingSetParameters.Builder())
                .setLegacyMode(true)
                .setConnectable(true)
                .setScannable(true)
                .setInterval((int)(advertiseConfiguration.interval/0.625))
                .setTxPowerLevel(advertiseConfiguration.txPower)
                .build();
        this.key = SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name();

        Device device = new DeviceRepository(database).getDevice();

        data = (new AdvertiseData.Builder())
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(device.uuid)
                .build();

        callback = new AdvertisingSetCallback(){
            // TODO: AdvertiseCallback:Puede implementarse en caso de que sea necesario hacer algo aca.
        };
    }

    @Override
    protected void startTask() {
        advertiser.startAdvertisingSet(parameters, data, null, null, null, callback);
    }

    @Override
    protected void endTask() {
        advertiser.stopAdvertisingSet(callback);
    }

    @Override
    public void stop() {
        super.stop();
        if(advertiser !=null) {
            advertiser.stopAdvertisingSet(callback);
            advertiser = null;
        }
    }
}
