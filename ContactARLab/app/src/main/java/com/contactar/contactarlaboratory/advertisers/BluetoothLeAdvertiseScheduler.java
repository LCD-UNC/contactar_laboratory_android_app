package com.contactar.contactarlaboratory.advertisers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.export.representations.DeviceRepresentation;
import com.contactar.contactarlaboratory.scanners.Scheduler;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;

import java.util.UUID;

public class BluetoothLeAdvertiseScheduler extends Scheduler {

    public static final ParcelUuid EXPERIMENT_SERVICE_UUID = new ParcelUuid(UUID.fromString("436f6e74-6163-7441-522d-457870424c45"));

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
                .setLegacyMode(false)
                .setInterval((int)(advertiseConfiguration.interval/0.625))
                .setTxPowerLevel(advertiseConfiguration.txPower)
                .build();
        this.key = SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name();

        Device device = new DeviceRepository(database).getDevice();
        byte[] deviceJson = new DeviceRepresentation(device).toJson().getBytes();

        data = (new AdvertiseData.Builder())
                .addServiceUuid(EXPERIMENT_SERVICE_UUID)
                .addServiceData(EXPERIMENT_SERVICE_UUID, deviceJson)
                .setIncludeTxPowerLevel(true).build();

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
