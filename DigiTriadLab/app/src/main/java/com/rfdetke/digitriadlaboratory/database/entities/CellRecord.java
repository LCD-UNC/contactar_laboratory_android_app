package com.rfdetke.digitriadlaboratory.database.entities;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cell_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class CellRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "technology")
    public String technology;
    @ColumnInfo(name = "dbm")
    public int dbm;
    @ColumnInfo(name = "asu_level")
    public int asuLevel;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public CellRecord(String technology, int dbm, int asuLevel) {
        this.technology = technology;
        this.dbm = dbm;
        this.asuLevel = asuLevel;
    }
}
