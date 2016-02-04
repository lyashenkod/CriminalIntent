package com.example.dima.criminalintent.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.dima.criminalintent.ui.fragments.CrimeCameraFragment;

/**
 * Created by Dima on 04.02.2016.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Скрытие заголовка окна.
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // Скрытие панели состояния и прочего оформления уровня ОС
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
