package com.contactar.contactarlaboratory.database.entities;

import android.os.ParcelUuid;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bluetooth_le_uuid",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "record_id")},
        foreignKeys = @ForeignKey(entity = BluetoothLeRecord.class,
                parentColumns = "id",
                childColumns = "record_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothLeUuid {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public ParcelUuid uuid;

    @ColumnInfo(name = "record_id")
    public long recordId;

    public BluetoothLeUuid(ParcelUuid uuid, long recordId) {
        this.uuid = uuid;
        this.recordId = recordId;
    }
}
