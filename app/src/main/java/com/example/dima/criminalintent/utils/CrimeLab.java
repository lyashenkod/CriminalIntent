package com.example.dima.criminalintent.utils;

import android.content.Context;
import android.util.Log;

import com.example.dima.criminalintent.entity.Crime;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Dima on 18.01.2016.
 */
public class CrimeLab {
    private ArrayList<Crime> mCrimes;


    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
      //  mCrimes = new ArrayList<Crime>();
//        for (int i = 0; i < 100; i++) {
//            Crime c = new Crime();
//            c.setTitle("Преступление #" + i);
//            c.setSolved(i % 2 == 0); // Для каждого второго объекта
//            mCrimes.add(c);
//        }

        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }
    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }
}
