package com.contactar.contactarlaboratory.export.csv;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.repositories.GpsRepository;

import java.util.ArrayList;
import java.util.List;

public class GpsCsvFileWriter extends CsvSampleFileWriter {

    public GpsCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new GpsRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.GPS.name();
    }
}
