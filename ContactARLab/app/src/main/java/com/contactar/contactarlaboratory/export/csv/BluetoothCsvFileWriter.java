package com.contactar.contactarlaboratory.export.csv;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.repositories.BluetoothRepository;

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