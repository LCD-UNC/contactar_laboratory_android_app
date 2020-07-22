package com.rfdetke.digitriadlaboratory.entities;

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

    public Device(String codename, String brand, String model) {
        this.codename = codename;
        this.brand = brand;
        this.model = model;
    }
}
