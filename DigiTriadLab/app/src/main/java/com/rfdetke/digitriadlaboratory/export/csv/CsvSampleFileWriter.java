package com.rfdetke.digitriadlaboratory.export.csv;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;

import java.util.Locale;

public abstract class CsvSampleFileWriter extends CsvFileWriter{

    protected final Experiment experiment;
    protected final long[] runIds;
    private final Device device;

    public CsvSampleFileWriter(long[] runIds, AppDatabase database, Context context) {
        super(database, context);
        this.runIds = runIds;
        Run run = new RunRepository(database).getById(runIds[0]);
        experiment = new ExperimentRepository(database).getById(run.experimentId);
        device = new DeviceRepository(database).getDevice();
    }

    @Override
    public String getPath() {
        return String.format(Locale.ENGLISH, "exports/%s/", experiment.codename.toLowerCase());
    }

    @Override
    public String getFileName() {
        if(runIds.length>1) {
            return String.format(Locale.ENGLISH, "multiple-runs-%s.csv", getKey().toLowerCase());
        } else {
            Run run = new RunRepository(database).getById(runIds[0]);
            return String.format(Locale.ENGLISH, "X-%s_R-%d_%s_%s.csv",
                    experiment.codename.toLowerCase(), run.number, getKey().toLowerCase(),
                    device.codename);
        }
    }

    public abstract String getKey();
}