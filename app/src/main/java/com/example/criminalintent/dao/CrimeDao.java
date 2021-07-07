package com.example.criminalintent.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.criminalintent.data.Crime;

import java.util.List;

@Dao
public interface CrimeDao {

    @Insert
    public void insetCrime(Crime ... crimes);

    @Update
    public void updateCrime(Crime ... crimes);

    @Delete
    public void deleteCrime(Crime ... crimes);

    @Query("select * from crime_table")
    public LiveData<List<Crime>> getAllCrimes();

    @Query("select * from crime_table")
    public List<Crime> getCrimes();

    @Query("select * from crime_table")
    public DataSource.Factory<Integer, Crime> loadAllCrime();

}
