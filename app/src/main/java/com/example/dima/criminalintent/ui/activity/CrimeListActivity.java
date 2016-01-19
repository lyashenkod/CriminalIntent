package com.example.dima.criminalintent.ui.activity;


import android.support.v4.app.Fragment;

import com.example.dima.criminalintent.ui.fragments.CrimeListFragment;

/**
 * Created by Dima on 18.01.2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
