package com.rfdetke.digitriadlaboratory.export;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeCsvFileWriter extends CsvFileWriter{

    public BluetoothLeCsvFileWriter(long runId, AppDatabase database, Context context) throws NullPointerException {
        super(runId, database, context);
    }

    @Override
    public List<CsvExportable> getExportables(AppDatabase database, long[] runs) {
        return new ArrayList<>(new BluetoothLeRepository(database).getAllSamples(runs));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.BLUETOOTH_LE.name();
    }
}
