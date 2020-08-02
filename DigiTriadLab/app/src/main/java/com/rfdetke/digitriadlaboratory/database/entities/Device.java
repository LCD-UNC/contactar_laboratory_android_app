package com.rfdetke.digitriadlaboratory.database.entities;

import android.os.ParcelUuid;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "device",
        indices = @Index(value = "id", unique = true))
public class Device {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String codename;
    public String brand;
    public String model;
    public ParcelUuid uuid;

    public Device(String codename, String brand, String model, ParcelUuid uuid) {
        this.codename = codename;
        this.brand = brand;
        this.model = model;
        this.uuid = uuid;
    }
}
