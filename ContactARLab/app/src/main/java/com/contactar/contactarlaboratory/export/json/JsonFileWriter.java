package com.contactar.contactarlaboratory.export.json;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.export.FileWriter;

import java.util.List;


public abstract class JsonFileWriter extends FileWriter {

    protected final AppDatabase database;

    public JsonFileWriter(Context context, AppDatabase database) {
        super(context);
        this.database = database;
    }

    @Override
    public String getContent() {
        List<JsonExportable> data = getExportableData();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(data);
    }

    public abstract List<JsonExportable> getExportableData();
}
