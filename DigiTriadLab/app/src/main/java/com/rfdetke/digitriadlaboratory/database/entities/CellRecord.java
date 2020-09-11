package com.rfdetke.digitriadlaboratory.database.entities;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;

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

    @ColumnInfo(name = "operator")
    public String operator;
    @ColumnInfo(name = "technology")
    public String technology;
    @ColumnInfo(name = "dbm")
    public int dbm;
    @ColumnInfo(name = "asu_level")
    public int asuLevel;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public CellRecord(CellInfo cellInfo, long windowId) {
        if (cellInfo.equals(CellInfoGsm.class)) {
            this.technology = "GSM";
            CellInfoGsm gsmCell = (CellInfoGsm) cellInfo;
            this.dbm = gsmCell.getCellSignalStrength().getDbm();
            this.asuLevel = gsmCell.getCellSignalStrength().getAsuLevel();
            this.operator = gsmCell.getCellIdentity().getOperatorAlphaLong().toString();
        } else if (cellInfo.equals(CellInfoWcdma.class)) {
            this.technology = "WCDMA";
            CellInfoWcdma wcdmaCell = (CellInfoWcdma) cellInfo;
            this.dbm = wcdmaCell.getCellSignalStrength().getDbm();
            this.asuLevel = wcdmaCell.getCellSignalStrength().getAsuLevel();
            this.operator = wcdmaCell.getCellIdentity().getOperatorAlphaLong().toString();

        } else if (cellInfo.equals(CellInfoLte.class)) {
            CellInfoLte lteCell = (CellInfoLte) cellInfo;
            this.technology = "LTE";
            this.dbm = lteCell.getCellSignalStrength().getDbm();
            this.asuLevel = lteCell.getCellSignalStrength().getAsuLevel();
            this.operator = lteCell.getCellIdentity().getOperatorAlphaLong().toString();
        }
        this.windowId = windowId;
    }

    public CellRecord(String operator, String technology, int dbm, int asuLevel, long windowId) {
        this.operator = operator;
        this.technology = technology;
        this.dbm = dbm;
        this.asuLevel = asuLevel;
        this.windowId = windowId;
    }
}
