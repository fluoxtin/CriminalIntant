package com.example.criminalintent.db;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.criminalintent.dao.CrimeDao;
import com.example.criminalintent.data.Crime;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * RoomDatabase is a database layer on top of sqlite
 */
@Database(entities = {Crime.class}, version = 1, exportSchema = false)
public abstract class LocalRoomDatabase extends androidx.room.RoomDatabase {

    public abstract CrimeDao mCrimeDao();

    /**
     * INSTANCE is a singleton, we will always get it from everywhere
     */

    private static volatile LocalRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LocalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context,
                            LocalRoomDatabase.class,
                            "crime_database"
                    ).addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static androidx.room.RoomDatabase.Callback sRoomDatabaseCallback = new androidx.room.RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            super.onCreate(database);
            databaseWriteExecutor.execute(() -> {
                CrimeDao dao = INSTANCE.mCrimeDao();

                for (int i = 0; i < 1000; i++) {
                    Crime crime = new Crime();
                    crime.setTitle("Crime # " + i);
//                    crime.setDate(new Date());
                    crime.setSolved(false);
                    crime.setSuspect(null);
                    dao.insetCrime(crime);
                }
            });
        }
    };
}
