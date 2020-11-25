package com.contactar.digitriadlaboratory.export.csv;

import android.content.Context;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.repositories.BluetoothRepository;

import java.util.ArrayList;
import java.util.List;

public class BluetoothCsvFileWriter extends CsvSampleFileWriter {

    public BluetoothCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new BluetoothRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.BLUETOOTH.name();
    }
}
