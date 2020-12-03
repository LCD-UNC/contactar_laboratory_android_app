package com.contactar.contactarlaboratory.export.csv;

import android.content.Context;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;
import com.contactar.contactarlaboratory.repositories.RunRepository;

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
            return String.format(Locale.ENGLISH, "X-%s_M-R_%s_%s.csv", experiment.codename.toLowerCase(), getKey().toLowerCase(), device.codename);
        } else {
            Run run = new RunRepository(database).getById(runIds[0]);
            return String.format(Locale.ENGLISH, "X-%s_R-%d_%s_%s.csv",
                    experiment.codename.toLowerCase(), run.number, getKey().toLowerCase(),
                    device.codename);
        }
    }

    public abstract String getKey();
}
