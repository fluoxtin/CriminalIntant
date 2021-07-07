package com.example.criminalintent.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.sqlite.CrimeBaseHelper;
import com.example.criminalintent.sqlite.CrimeCursorWrapper;
import java.util.ArrayList;
import java.util.List;
import static com.example.criminalintent.sqlite.CrimeDbSchema.*;

public class CrimeLab {

    private static CrimeLab sCrimeLab;     //创建一个单例
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab =  new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){

        mContext = context.getApplicationContext();
        //调用getWritableDatabase()方法时，CrimeBaseHelper会
        //先打开crimeBase.db,如果不存在，先创建该文件
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c) {   // 定义新方法向crime列表添加新的crime
//        mCrimes.add(c);               // 以响应用户点击New Crime菜单项
        ContentValues values = getContentValues(c);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, values);
    }
    //用来返回数组列表
    public List<Crime> getCrimes() {
//        return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null,null);
        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;
    }

    //返回带指定ID的Crime对象
    public Crime getCrime(int id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.ID + " = ? ",
                new String[] { String.valueOf(id) }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String idString = String.valueOf(crime.getId());
        ContentValues values = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    public void deleteCrime(Crime crime) {
        String idString = String.valueOf(crime.getId());
        ContentValues values = getContentValues(crime);
        mSQLiteDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.ID, String.valueOf(crime.getId()));
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }
}
