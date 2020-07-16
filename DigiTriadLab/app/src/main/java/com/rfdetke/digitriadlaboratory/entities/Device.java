package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device")
public class Device {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String codename;
    public String brand;
    public String model;
}
