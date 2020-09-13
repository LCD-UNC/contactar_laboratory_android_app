package com.rfdetke.digitriadlaboratory;

import android.os.ParcelUuid;

import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket.SENSOR_TYPES;

public class TestUtils {

    private static final Random rand = new Random();
    private static final int MAX_ACTIVE = 2;
    private static final int MAX_INACTIVE = 2;
    private static final int MAX_WINDOWS = 2;
    private static final int MIN_INTERVAL = 100;
    private static final int MAX_INTERVAL = 10485759;
    private static final int MIN_PERIODIC_INTERVAL = 6;
    private static final int MAX_PERIODIC_INTERVAL = 65536;
    private static final int MIN_TX_POWER = -127;
    private static final int MAX_TX_POWER = 1;
    private static final int MIN_RSSI = -127;
    private static final int MAX_RSSI = 126;
    private static final int MIN_LIST_SIZE = 4;
    private static final int MAX_LIST_SIZE = 20;
    private static final int MAX_UUID_LIST_SIZE = 4;
    private static final int[] MAJOR_CLASSES = { 0x0000, 0x0100, 0x0200, 0x0300, 0x0400, 0x0500,
                                                    0x0600, 0x0700, 0x0800, 0x0900, 0x1F00 };

    public static Device getDevice() {
        return new Device(RandomStringUtils.randomAlphanumeric(7),
                            RandomStringUtils.randomAlphanumeric(5),
                            RandomStringUtils.randomAlphanumeric(3),
                            getRandomParcelUuid());
    }

    public static Experiment getExperiment(long deviceId) {
        return new Experiment(RandomStringUtils.randomAlphanumeric(6),
                                RandomStringUtils.randomAlphanumeric(125),
                                deviceId);
    }

    public static Run getRun(long last, long experimentId) {
        return new Run(new Date(),
                last+1,
                        RunStateEnum.SCHEDULED.name(),
                        experimentId);
    }

    public static List<Run> getRunList(long experimentId) {
        List<Run> runs = new ArrayList<>();
        int size = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < size; i++) {
            runs.add(getRun(i, experimentId));
        }
        return runs;
    }

    public static WindowConfiguration getWindowConfiguration(long sourceType, long experimentId) {
        return new WindowConfiguration(ThreadLocalRandom.current().nextInt(1, MAX_ACTIVE),
                                        ThreadLocalRandom.current().nextInt(1, MAX_INACTIVE),
                                        ThreadLocalRandom.current().nextInt(1, MAX_WINDOWS),
                                        sourceType,
                                        experimentId);
    }

    public static SourceType getSourceType() {
        return new SourceType(getRandomString(15));
    }

    public static AdvertiseConfiguration getAdvertiseConfiguration(long experimentId) {
        return new AdvertiseConfiguration( getRandomTxPower(),
                                            getRandomInterval(),
                                            experimentId );
    }

    public static BluetoothLeRecord getBluetoothLeRecord(long windowId) {
        return new BluetoothLeRecord( randomMACAddress(),                                           //address
                                        getRandomRssi(),                                            //rssi
                                        getRandomTxPower(),                                         //txPower
                                        getRandomInt(),                                             //advertisingSetID
                                        BluetoothLeRecord.parsePhysicalLayer(getRandomInt(0,4)),    //phyLayer1
                                        BluetoothLeRecord.parsePhysicalLayer(getRandomInt(0,4)),    //phyLayer2
                                        getRandomPeriodicAdvertisingInterval(),                     //advertisingInterval
                                        getRandomInt(0,2),                                          //connectable
                                        getRandomInt(0,2),                                          //legacy
                                        windowId );                                                 //windowId
    }

    public static List<BluetoothLeRecord> getBluetoothLeRecordList(long windowId) {
        List<BluetoothLeRecord> records = new ArrayList<>();
        int size = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < size; i++) {
            records.add(getBluetoothLeRecord(windowId));
        }
        return records;
    }


    public static BluetoothRecord getBluetoothRecord(long windowId) {
        return new BluetoothRecord( randomMACAddress(),
                                    BluetoothRecord.parseBluetoothMajorClass(getRandomBluetoothMajorClass()),
                                    getRandomInt(0,2),
                                    BluetoothRecord.parseType(getRandomInt(0,4)),
                                    windowId );
    }

    public static List<BluetoothRecord> getBluetoothRecordList(long windowId) {
        List<BluetoothRecord> records = new ArrayList<>();
        int size = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < size; i++) {
            records.add(getBluetoothRecord(windowId));
        }
        return records;
    }

    public static WifiRecord getWifiRecord(long windowId) {
        return new WifiRecord( randomMACAddress(),
                                WifiRecord.parseChannelWidth(getRandomInt(0,5)),
                                getRandomInt(0,5500),
                                getRandomInt(0,5500),
                                getRandomInt(0,5500),
                                getRandomRssi(),
                                getRandomInt(0,2),
                                windowId );
    }

    public static List<WifiRecord> getWifiRecordList(long windowId) {
        List<WifiRecord> records = new ArrayList<>();
        int size = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < size; i++) {
            records.add(getWifiRecord(windowId));
        }
        return records;
    }

    public static SensorRecord getSensorRecord(long windowId) {
        SensorRecord record = new SensorRecord( SensorRecord.parseSensorType(getRandomSensorType()),
                getRandomInt(0,6),
                getRandomDouble());
        record.windowId = windowId;
        return record;
    }

    public static List<SensorRecord> getSensorRecordList(long windowId) {
        List<SensorRecord> records = new ArrayList<>();
        int size = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < size; i++) {
            records.add(getSensorRecord(windowId));
        }
        return records;
    }

    public static BluetoothLeUuid getBluetoothLeUuid(long recordId) {
        return new BluetoothLeUuid(getRandomParcelUuid(), recordId);
    }

    public static List<BluetoothLeUuid> getBluetoothLeUuidList(long[] recordId) {
        List<BluetoothLeUuid> uuids = new ArrayList<>();
        for (long l : recordId) {
            int size = getRandomInt(0, MAX_UUID_LIST_SIZE);
            for (int i = 0; i < size; i++) {
                uuids.add(getBluetoothLeUuid(l));
            }
        }
        return uuids;
    }

    public static double getRandomDouble() {
        return getRandomInt()/(double)getRandomInt();
    }
    private static int getRandomSensorType() {
        int index = getRandomInt(0, SENSOR_TYPES.length);
        return SENSOR_TYPES[index];
    }

    private static int getRandomBluetoothMajorClass() {
        int index = getRandomInt(0, MAJOR_CLASSES.length);
        return MAJOR_CLASSES[index];
    }

    public static ParcelUuid getRandomParcelUuid() {
        return new ParcelUuid(UUID.randomUUID());
    }

    public static int getRandomTxPower() {
        return ThreadLocalRandom.current().nextInt(MIN_TX_POWER, MAX_TX_POWER);
    }

    public static int getRandomInterval() {
        return ThreadLocalRandom.current().nextInt(MIN_INTERVAL, MAX_INTERVAL);
    }

    public static double getRandomPeriodicAdvertisingInterval() {
        return ThreadLocalRandom.current().nextInt(MIN_PERIODIC_INTERVAL, MAX_PERIODIC_INTERVAL)*1.25;
    }

    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static int getRandomRssi() {
        return ThreadLocalRandom.current().nextInt(MIN_RSSI, MAX_RSSI);
    }

    public static String getRandomString(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    public static List<String> getRandomStringList(int stringMin, int stringMax) {
        List<String> strings = new ArrayList<>();
        int arraySize = getRandomInt(MIN_LIST_SIZE, MAX_LIST_SIZE);
        for (int i = 0; i < arraySize; i++) {
            int stringSize = getRandomInt(stringMin,stringMax);
            strings.add(getRandomString(stringSize));
        }
        return strings;
    }

    public static String randomMACAddress(){
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte)(macAddr[0] & (byte)254);  //zeroing last 2 bytes to make it unicast and locally adminstrated

        StringBuilder sb = new StringBuilder(18);
        for(byte b : macAddr){

            if(sb.length() > 0)
                sb.append(":");

            sb.append(String.format("%02x", b));
        }


        return sb.toString();
    }
}
