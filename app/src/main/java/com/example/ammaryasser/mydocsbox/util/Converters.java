package com.example.ammaryasser.mydocsbox.util;
/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import static com.example.ammaryasser.mydocsbox.util.Utils.formatDate;


public class Converters {

    @TypeConverter
    public static List<Integer> fromString(String value) {
        if (value == null) return null;

        Type listType = new TypeToken<List<Integer>>() {
        }.getType();

        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Integer> list) {
        if (list == null) return null;

        return new Gson().toJson(list);
    }


    @TypeConverter
    public static String timestampToStrDate(Long ts) {
        return ts == null ? null : formatDate(new Date(ts));
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}

