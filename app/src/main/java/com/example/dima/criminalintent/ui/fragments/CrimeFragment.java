package com.example.dima.criminalintent.ui.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.dima.criminalintent.R;
import com.example.dima.criminalintent.entity.Crime;
import com.example.dima.criminalintent.entity.Photo;
import com.example.dima.criminalintent.ui.activity.CrimeCameraActivity;
import com.example.dima.criminalintent.ui.fragments.dialog.DatePickerFragment;
import com.example.dima.criminalintent.ui.fragments.dialog.ImageFragment;
import com.example.dima.criminalintent.ui.fragments.dialog.TimePickerFragment;
import com.example.dima.criminalintent.utils.CrimeLab;
import com.example.dima.criminalintent.utils.PictureUtils;

import java.io.File;
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
    private Button mDeleteButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Intent intent;

    private static final String DIALOG_IMAGE = "image";
    public static final String EXTRA_CRIME_ID =
            "com.example.dima.criminalintent.ui.fragments.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String TAG = "CrimeFragment";
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public void updateDate() {
        mDateButton.setText(DateFormat(mCrime.getDate()));
    }

    public CrimeFragment() {
        mCrime = new Crime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }


    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        mDeleteButton = (Button) v.findViewById(R.id.crime_delete);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && getActivity().getActionBar() != null) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleFied = (EditText) v.findViewById(R.id.crime_title);
        if (mCrime.getTitle() != null) {
            mTitleFied.setText(mCrime.getTitle().toString());
        }
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

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
            }
        });


        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                //startActivity(i);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });
        // Если камера недоступна, заблокировать функциональность
        // работы с камерой
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            mPhotoButton.setEnabled(false);
        }

        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();
                if (p == null)
                    return;
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                String path = getActivity()
                        .getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path)
                        .show(fm, DIALOG_IMAGE);
            }
        });
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                delPhotoDialog();
                return true;
            }
        });


        return v;
    }


    private void delPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setTitle(R.string.delete_photo )
                .setCancelable(false)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCrime.delPhoto(getActivity());
                        PictureUtils.cleanImageView(mPhotoView);
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    private void showPhoto() {
        // Назначение изображения, полученного на основе фотографии
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }




    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        intent = data;
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

        } else if (requestCode == REQUEST_PHOTO) {
            // Создание нового объекта Photo и связывание его с Crime
            String filename = data
                    .getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                //   Log.i(TAG, "filename: " + filename);
                if (mCrime.getPhoto()==null) {
                    Photo photo = new Photo(filename);
                    mCrime.setPhoto(photo);
                }else {
                    mCrime.delPhoto(getActivity());
                    Photo photo = new Photo(filename);
                    mCrime.setPhoto(photo);
                }
                //    Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
                showPhoto();
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                // Будет реализовано позднее
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }
}
