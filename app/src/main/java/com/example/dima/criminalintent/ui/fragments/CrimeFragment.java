package com.example.dima.criminalintent.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.dima.criminalintent.R;
import com.example.dima.criminalintent.entity.Crime;
import com.example.dima.criminalintent.ui.fragments.dialog.DatePickerFragment;
import com.example.dima.criminalintent.ui.fragments.dialog.TimePickerFragment;
import com.example.dima.criminalintent.utils.CrimeLab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Dima on 17.01.2016.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleFied;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    public static final String EXTRA_CRIME_ID =
            "com.example.dima.criminalintent.ui.fragments.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";


    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public void updateDate() {
        mDateButton.setText(DateFormat(mCrime.getDate()));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = new Crime();
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);
        mTitleFied = (EditText) v.findViewById(R.id.crime_title);
        mTitleFied.setText(mCrime.getTitle().toString());
        mTitleFied.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        //mDateButton.setText(DateFormat(mCrime.getDate()));
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment Datedialog = DatePickerFragment.newInstanse(mCrime.getDate());
                Datedialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                Datedialog.show(fm, DIALOG_DATE);

            }


        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            TimePickerFragment timeDialog = TimePickerFragment.newInstanse(mCrime.getDate());
            timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
            timeDialog.show(fm, DIALOG_TIME);

        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateDate();

        }
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String DateFormat(Date date) {
        Locale local = new Locale("ru", "RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, local);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return df.format(date) + " " + simpleDateFormat.format(date);
    }


}
