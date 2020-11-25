package com.contactar.digitriadlaboratory.export.csv;

import android.content.Context;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.repositories.SensorRepository;

import java.util.ArrayList;
import java.util.List;

public class SensorCsvFileWriter extends CsvSampleFileWriter {

    public SensorCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new SensorRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return "SENSORS";
    }
}
