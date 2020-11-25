package com.contactar.digitriadlaboratory.export.csv;

import android.content.Context;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.export.FileWriter;

import java.util.List;

public abstract class CsvFileWriter extends FileWriter {

    protected final AppDatabase database;

    public CsvFileWriter(AppDatabase database, Context context) {
        super(context);
        this.database = database;
    }

    @Override
    public String getContent() {
        List<CsvExportable> data = getExportableData();
        String content = "";
        if (data.size() != 0) {
            content = data.get(0).csvHeader();
            for (CsvExportable sample : data) {
                content = content.concat(sample.toCsv());
            }
        }
        return content;
    }

    public abstract List<CsvExportable> getExportableData();

}
