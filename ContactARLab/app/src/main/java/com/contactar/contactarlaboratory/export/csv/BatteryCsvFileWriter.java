package com.contactar.contactarlaboratory.export.csv;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.repositories.BatteryRepository;

import java.util.ArrayList;
import java.util.List;

public class BatteryCsvFileWriter extends CsvSampleFileWriter {

    public BatteryCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new BatteryRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.BATTERY.name();
    }
}
