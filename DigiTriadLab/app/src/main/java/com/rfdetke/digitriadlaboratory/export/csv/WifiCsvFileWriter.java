package com.rfdetke.digitriadlaboratory.export.csv;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;

import java.util.ArrayList;
import java.util.List;

public class WifiCsvFileWriter extends CsvFileWriter{

    public WifiCsvFileWriter(long runId, AppDatabase database, Context context) throws NullPointerException {
        super(runId, database, context);
    }

    @Override
    public List<CsvExportable> getExportables(AppDatabase database, long[] runs) {
        return new ArrayList<>(new WifiRepository(database).getAllSamplesFor(runs));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.WIFI.name();
    }
}
