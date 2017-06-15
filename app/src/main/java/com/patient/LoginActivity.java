package com.patient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.patient.framework.model.Patient;
import com.patient.framework.model.Queue;
import com.patient.framework.model.User;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.repository.QueueRepository;
import com.patient.framework.repository.UserRepository;
import com.patient.framework.service.UserServices;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    private SharedPreferences sharedPreferences;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private UserRepository userRepository;
    private PatientRepository patientRepository;
    private QueueRepository queueRepository;
    private User current;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("valid",false)){
            if(sharedPreferences.getBoolean("queue",false)){
                Intent[] intents={new Intent(LoginActivity.this,Main2Activity.class),
                        new Intent(LoginActivity.this,MainActivity.class)};
                startActivities(intents);
                finish();
            }else{
                startActivity(new Intent(LoginActivity.this,Main2Activity.class));
                finish();
            }
        }

        userRepository=UserRepository.getInstance(LoginActivity.this);
        queueRepository=new QueueRepository(LoginActivity.this);
        current=new User();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_login);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password_login);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignupButton = (Button) findViewById(R.id.sign_up_button);
        mSignupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                finish();
                startActivity(intent);
            }
        });

        CheckBox mCheckbox = (CheckBox) findViewById(R.id.login_checkBox);
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finish();
                    startActivity(new Intent(LoginActivity.this,resetActivity.class));
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void setSharedPreferences(){
        User currentUser=userRepository.getUser(email);
        String card = userRepository.getUser(current.getEml()).getCard();
        patientRepository=new PatientRepository(LoginActivity.this);
        Patient currentPatient=patientRepository.getPatient(card);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",currentPatient.getName());
        editor.putString("card",card);
        editor.putString("eml",email);
        editor.putString("psw",currentUser.getPsw());
        editor.putString("phone",currentUser.getPhone());
        editor.putInt("pid",currentPatient.getId());
        editor.putString("gender",currentPatient.getGender());
        editor.putInt("age",currentPatient.getAge());
        editor.putBoolean("valid",true);
        if(queueRepository.getQueue(currentPatient.getId(),1)!=null){
            editor.putBoolean("reg",true);
            editor.putBoolean("queue",true);
        }else{
            editor.putBoolean("reg",false);
            editor.putBoolean("queue",false);
        }
        int imageSource=R.drawable.user_young;
        if("男".equals(currentPatient.getGender())||"Male".equals(currentPatient.getGender())){
            imageSource=R.drawable.user_male;
        }else if("女".equals(currentPatient.getGender())||"Female".equals(currentPatient.getGender())){
            imageSource=R.drawable.user_female;
        }
        editor.putInt("avatar",imageSource);
        editor.commit();
    }


    private boolean attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        current.setEml(email);
        current.setPsw(password);
        Log.d("current",current.toString());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            switch(userRepository.isValid(current)) {
                case 1:{
                    Log.d("login","success");
                    String card = userRepository.getUser(current.getEml()).getCard();
                    setSharedPreferences();
                    if(patientRepository.checkPatient(card)){
                        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,Main2Activity.class);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(this,"请完善您的基本资料",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,
                                CompleteInfoActivity.class);
                        intent.putExtra("card",card);
                        finish();
                        startActivity(intent);
                    }
                    return true;
                }
                case -1: {
                    Log.d("login","not exist");
                    Toast.makeText(this,"用户不存在",Toast.LENGTH_SHORT).show();
                    mEmailView.requestFocus();
                    return false;
                }
                default:{
                    Log.d("login","fail");
                    Toast.makeText(this,"登录失败，邮箱或密码输入错误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

