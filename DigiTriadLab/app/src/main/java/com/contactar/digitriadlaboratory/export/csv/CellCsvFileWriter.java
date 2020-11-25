package com.contactar.digitriadlaboratory.export.csv;

import android.content.Context;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.repositories.CellRepository;

import java.util.ArrayList;
import java.util.List;

public class CellCsvFileWriter extends CsvSampleFileWriter {

    public CellCsvFileWriter(long[] runIds, AppDatabase database, Context context) throws NullPointerException {
        super(runIds, database, context);
    }

    @Override
    public List<CsvExportable> getExportableData() {
        return new ArrayList<>(new CellRepository(database).getAllSamples(runIds));
    }

    @Override
    public String getKey() {
        return SourceTypeEnum.CELL.name();
    }
}
