package com.contactar.digitriadlaboratory.database.typeconverters;

import android.os.ParcelUuid;

import androidx.room.TypeConverter;

public class UuidConverter {

    @TypeConverter
    public static String uuidToString(ParcelUuid uuid) {
        return uuid==null ? null : uuid.toString();
    }

    @TypeConverter
    public static ParcelUuid stringToUuid(String uuid) {
        return uuid==null ? null : ParcelUuid.fromString(uuid);
    }
}
