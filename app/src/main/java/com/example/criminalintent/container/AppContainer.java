package com.example.criminalintent.container;

import android.content.Context;

import com.example.criminalintent.SingletonContext;
import com.example.criminalintent.db.CrimeRepository;

public class AppContainer {

    public CrimeRepository mCrimeRepository = new CrimeRepository(SingletonContext.sContext);

}
