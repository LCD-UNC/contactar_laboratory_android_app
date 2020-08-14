package com.rfdetke.digitriadlaboratory.export.csv;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothRepository;

import java.util.ArrayList;
import java.util.List;

public class BluetoothCsvFileWriter extends CsvFileWriter{

    public BluetoothCsvFileWriter(long runId, AppDatabase database, Context context) throws NullPointerException {
        super(runId, database, context);
    }

    @Override
    public List<CsvExportable> getExportables(AppDatabase database, long[] runs) {
        return new ArrayList<>(new BluetoothRepository(database).getAllSamples(runs));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.BLUETOOTH.name();
    }
}
