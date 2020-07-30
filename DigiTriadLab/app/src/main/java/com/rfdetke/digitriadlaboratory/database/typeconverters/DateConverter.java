package com.rfdetke.digitriadlaboratory.database.typeconverters;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date==null ? null : df.format(date);
    }

    @TypeConverter
    public static Date fromTimestamp(String date) {
        try {
            return date==null ? null : df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
