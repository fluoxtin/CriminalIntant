package com.example.criminalintent;

import android.content.Context;

public class SingletonContext {
    public static Context sContext;
    public SingletonContext(Context context) {
        sContext = context;
    }

}
