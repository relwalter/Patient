package com.patient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.DatePicker.OnDateChangedListener;
import com.patient.framework.model.Patient;
import com.patient.framework.repository.PatientRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class CompleteInfoActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mNameView,mGenderView;
    private TextView mBirthView;
    private Button mCompleteButton;
    private PatientRepository patientRepository;
    private Patient current;
    private String card;
    private int nowYear,nowMonth,nowDay,year,month,day;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        patientRepository=new PatientRepository(this);
        current=new Patient();
        card=getIntent().getStringExtra("card");
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name_signup);
        mGenderView = (EditText) findViewById(R.id.gender_signup);
        mBirthView = (TextView) findViewById(R.id.birth_complete);
        Calendar c = Calendar.getInstance();
        nowYear = c.get(Calendar.YEAR);
        nowMonth = c.get(Calendar.MONTH)+1;
        nowDay = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        mBirthView.setText(year+" - "+(month+1)+" - "+day);
        mCompleteButton = (Button) findViewById(R.id.sign_up_button);
        mCompleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attemptComplete()){
                    Intent intent=new Intent(CompleteInfoActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mBirthView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mBirthView.setText(year+" - "+(monthOfYear+1)+" - "+dayOfMonth);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(CompleteInfoActivity.this,R.style.DiaglogTheme,listener,Integer.parseInt(mBirthView.getText().toString().split(" - ")[0]),Integer.parseInt(mBirthView.getText().toString().split(" - ")[1])-1,Integer.parseInt(mBirthView.getText().toString().split(" - ")[2]));
                dialog.show();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptComplete() {

        // Reset errors.
        mNameView.setError(null);
        mGenderView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String gender = mGenderView.getText().toString();
        String birth = mBirthView.getText().toString();
        int yearOfBirth = Integer.parseInt(birth.split(" - ")[0]);
        int monthOfBirth = Integer.parseInt(birth.split(" - ")[1]);
        int dayOfBirth = Integer.parseInt(birth.split(" - ")[2]);
        current.setCard(card);
        current.setName(name);
        current.setGender(gender);
        current.setAge(nowYear-yearOfBirth);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(gender)) {
            mGenderView.setError(getString(R.string.error_field_required));
            focusView = mGenderView;
            cancel = true;
        }

        if (!TextUtils.equals(gender.toString(),"男".toString())||!TextUtils.equals(gender.toString(),"女".toString()) ||!TextUtils.equals(gender.toString(),"Male".toString())||!TextUtils.equals(gender.toString(),"Female".toString())) {
            mGenderView.setError("格式不正确");
            focusView = mGenderView;
            cancel = true;
        }

        if (yearOfBirth>nowYear
                ||(yearOfBirth==nowYear&&monthOfBirth>nowMonth)||(yearOfBirth==nowYear&&monthOfBirth==nowMonth&&dayOfBirth>nowDay)) {
            mBirthView.setError("出生日期不能大于目前日期");
            focusView = mBirthView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            switch(patientRepository.addPatient(current)){
                case 1:{
                    Toast.makeText(this,"注册成功，即将返回登录界面",Toast.LENGTH_SHORT).show();
                    return true;
                }
                case -1: {
                    Toast.makeText(this,"资料已存在，正在跳过此步骤",Toast.LENGTH_SHORT).show();
                    return false;
                }
                default:{
                    Toast.makeText(this,"注册失败，请稍候再次重试",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }

}

