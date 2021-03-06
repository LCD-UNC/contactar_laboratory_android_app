package com.contactar.contactarlaboratory.database.typeconverters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
//    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);

//    @TypeConverter
//    public static String dateToTimestamp(Date date) {
//        return date==null ? null : df.format(date);
//    }
//
//    @TypeConverter
//    public static Date fromTimestamp(String date) {
//        try {
//            return date==null ? null : df.parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date==null ? null : date.getTime();
    }

}
