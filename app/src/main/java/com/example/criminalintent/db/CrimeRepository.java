package com.example.criminalintent.db;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.criminalintent.dao.CrimeDao;
import com.example.criminalintent.data.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CrimeRepository {

    public CrimeDao mCrimeDao;
    private LiveData<List<Crime>> mListLiveData;
    private List<Crime> mCrimes;

    public CrimeRepository(Context context) {
        mCrimeDao = LocalRoomDatabase.getDatabase(context).mCrimeDao();
    }

    public LiveData<List<Crime>> getListLiveData() {
        if (mListLiveData == null) {
            mListLiveData =  mCrimeDao.getAllCrimes();
        }
        return mListLiveData;
    }

    public List<Crime> getListCrimes() {
        if (mCrimes == null) {
            mCrimes = mCrimeDao.getCrimes();
        }
        return mCrimes;
    }


    public void insertCrime(Crime crime) {
        mCrimeDao.insetCrime(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimeDao.deleteCrime(crime);
    }

    public void updateCrime(Crime crime) {
        mCrimeDao.updateCrime(crime);
    }
}
