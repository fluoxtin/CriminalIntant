package com.example.criminalintent.data;

import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDConvert {
    //TODO convert uuid to int

    @TypeConverter
    public static UUID toUUID(String string) {
        return UUID.fromString(string) ;
    }

    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString();
    }
}
