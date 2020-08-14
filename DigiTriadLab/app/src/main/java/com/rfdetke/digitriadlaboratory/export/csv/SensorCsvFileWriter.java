package com.rfdetke.digitriadlaboratory.export.csv;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.SensorRepository;

import java.util.ArrayList;
import java.util.List;

public class SensorCsvFileWriter extends CsvFileWriter{

    public SensorCsvFileWriter(long runId, AppDatabase database, Context context) throws NullPointerException {
        super(runId, database, context);
    }

    @Override
    public List<CsvExportable> getExportables(AppDatabase database, long[] runs) {
        return new ArrayList<>(new SensorRepository(database).getAllSamplesFor(runs));
    }

    @Override
    public String getKey() {
        return "SENSORS";
    }
}
