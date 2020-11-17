package com.rfdetke.digitriadlaboratory.export.csv;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.ActivityRepository;
import com.rfdetke.digitriadlaboratory.repositories.BatteryRepository;

import java.util.ArrayList;
import java.util.List;

public class ActivityCsvFileWriter extends CsvSampleFileWriter {

    public ActivityCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new ActivityRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.ACTIVITY.name();
    }
}
