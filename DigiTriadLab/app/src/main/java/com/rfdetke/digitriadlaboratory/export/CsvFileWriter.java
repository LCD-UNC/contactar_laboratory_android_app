package com.rfdetke.digitriadlaboratory.export;

import android.content.Context;
import android.os.AsyncTask;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

public abstract class CsvFileWriter extends AsyncTask<Void, Void, Void> {
    private final Run run;
    private final Experiment experiment;
    private final File appDir;
    private final List<CsvExportable> samples;
    private final String key;

    public CsvFileWriter(long runId, AppDatabase database, Context context) throws NullPointerException {
        try {
            run = new RunRepository(database).getById(runId);
            experiment = new ExperimentRepository(database).getById(run.experimentId);
            long[] runs = {runId};
            samples = getExportables(database, runs);
            key = getKey();
            appDir = new File(context.getExternalFilesDir(null)+"/exports/");
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(run != null && experiment != null && appDir != null && key != null && !samples.isEmpty()){
            String fileName = String.format(Locale.ENGLISH, "run-%d-%s.csv", run.number, key.toLowerCase());
            File folder = new File(appDir, experiment.codename.toLowerCase());
            boolean success = folder.mkdirs();
            File file = new File(folder, fileName);
            String content = samples.get(0).csvHeader();
            for( CsvExportable sample : samples) {
                content = content.concat(sample.toCsv());
            }

            try {
                OutputStreamWriter outputWriter = new OutputStreamWriter(new FileOutputStream(file, false));
                outputWriter.write(content);
                outputWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public abstract List<CsvExportable> getExportables(AppDatabase database, long[] runs);
    public abstract String getKey();
}
