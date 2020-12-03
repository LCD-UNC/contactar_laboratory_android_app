package com.contactar.contactarlaboratory.database.entities;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;

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
        if (cellInfo instanceof CellInfoGsm) {
            this.technology = "GSM";
            CellInfoGsm gsmCell = (CellInfoGsm) cellInfo;
            this.dbm = gsmCell.getCellSignalStrength().getDbm();
            this.asuLevel = gsmCell.getCellSignalStrength().getAsuLevel();
            this.operator = gsmCell.getCellIdentity().getOperatorAlphaLong().toString();
        } else if (cellInfo instanceof CellInfoWcdma) {
            this.technology = "WCDMA";
            CellInfoWcdma wcdmaCell = (CellInfoWcdma) cellInfo;
            this.dbm = wcdmaCell.getCellSignalStrength().getDbm();
            this.asuLevel = wcdmaCell.getCellSignalStrength().getAsuLevel();
            this.operator = wcdmaCell.getCellIdentity().getOperatorAlphaLong().toString();

        } else if (cellInfo instanceof CellInfoLte) {
            CellInfoLte lteCell = (CellInfoLte) cellInfo;
            this.technology = "LTE";
            this.dbm = lteCell.getCellSignalStrength().getDbm();
            this.asuLevel = lteCell.getCellSignalStrength().getAsuLevel();
            this.operator = lteCell.getCellIdentity().getOperatorAlphaLong().toString();
        } else if (cellInfo instanceof CellInfoCdma) {
            CellInfoCdma lteCell = (CellInfoCdma) cellInfo;
            this.technology = "CDMA";
            this.dbm = lteCell.getCellSignalStrength().getDbm();
            this.asuLevel = lteCell.getCellSignalStrength().getAsuLevel();
            this.operator = lteCell.getCellIdentity().getOperatorAlphaLong().toString();
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr) {
            CellInfoNr lteCell = (CellInfoNr) cellInfo;
            this.technology = "5G NR";
            this.dbm = lteCell.getCellSignalStrength().getDbm();
            this.asuLevel = lteCell.getCellSignalStrength().getAsuLevel();
            this.operator = lteCell.getCellIdentity().getOperatorAlphaLong().toString();
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            CellInfoTdscdma lteCell = (CellInfoTdscdma) cellInfo;
            this.technology = "TD-SCDMA";
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
