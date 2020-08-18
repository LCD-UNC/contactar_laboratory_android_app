package com.rfdetke.digitriadlaboratory.export;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class FileWriter extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private File file;
    private OutputStreamWriter outputWriter;
    File folder;

    public FileWriter(Context context) {
        this.context = context;
    }

    public abstract String getContent();
    public abstract String getPath();
    public abstract String getFileName();

    @Override
    protected Void doInBackground(Void... voids) {
        folder = new File(context.getExternalFilesDir(null)+"/"+getPath());
        boolean success = folder.mkdirs();
        file = new File(folder, getFileName());
        try {
            outputWriter = new OutputStreamWriter(new FileOutputStream(file, false));
            outputWriter.write(getContent());
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
