package com.patient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
    private DatePicker mBdayPicker;
    private View mProgressView;
    private View mCompleteFormView;
    private PatientRepository patientRepository;
    private Patient current;
    private String card=getIntent().getExtras().getString("card");
    private int nowYear,year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        patientRepository=new PatientRepository(this);
        current=new Patient();
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name_signup);
        mGenderView = (EditText) findViewById(R.id.gender_signup);
        mBdayPicker = (DatePicker) findViewById(R.id.signup_datePicker);
        Calendar c = Calendar.getInstance();
        nowYear = c.get(Calendar.YEAR);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        mBdayPicker.init(year,month,day,new OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                CompleteInfoActivity.this.year=year;
                CompleteInfoActivity.this.month=monthOfYear;
                CompleteInfoActivity.this.day=dayOfMonth;
            }
        });
        Button mCompleteButton = (Button) findViewById(R.id.complete_button);
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

        mCompleteFormView = findViewById(R.id.complete_form);
        mProgressView = findViewById(R.id.login_progress);
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
        current.setCard(card);
        current.setName(name);
        current.setGender(gender);
        current.setAge(nowYear-year);

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            switch(patientRepository.addPatient(current)){
                case 1:{
                    Toast.makeText(this,"注册成功，即将返回登录界面",Toast.LENGTH_SHORT);
                    return true;
                }
                case -1: {
                    Toast.makeText(this,"资料已存在，正在跳过此步骤",Toast.LENGTH_SHORT);
                    return false;
                }
                default:{
                    Toast.makeText(this,"注册失败，请稍候再次重试",Toast.LENGTH_SHORT);
                    return false;
                }
            }
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCompleteFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCompleteFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCompleteFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCompleteFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

