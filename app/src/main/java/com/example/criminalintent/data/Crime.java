package com.example.criminalintent.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Entity(tableName = "crime_table")
public class Crime implements Serializable {                //模型层Crime类
    @PrimaryKey
    @NonNull
    @TypeConverters(UUIDConvert.class)
    private UUID mId;
    @ColumnInfo
    private String mTitle;
    @ColumnInfo
    @TypeConverters(DateConverter.class)
    private Date mDate;
    @ColumnInfo
    private boolean mSolved;
    @ColumnInfo
    private String mSuspect;

    public Crime() {
        mId = UUID.randomUUID(); //随机产生一个ID值，产生唯一ID
        mDate = new Date();
    }

    public void setId(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }
}
